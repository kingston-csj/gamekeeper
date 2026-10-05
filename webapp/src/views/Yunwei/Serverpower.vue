<template>
    <div class="backup-page">
        <el-card shadow="hover" class="top-panel">
            <div slot="header" class="panel-header">
                <span>服务器操作</span>
            </div>
            <div
                style="background: #f5f7fa; padding: 16px; border-radius: 6px; margin-bottom: 24px; color: #606266; line-height: 1.7;">
                <p style="font-weight: 600; margin:0 0 8px 0;">📌 功能说明</p>
                <p style="margin:0;">1. 操作会直接影响服务器运行，业务会中断，请确认；</p>
                <p style="margin:0;">2. 启动/重启/关闭服务器属于高危操作；</p>
                <!-- <p style="margin:0;">3. 一键更新服务器代码或配置文件（此操作仅触发动作，详细日志请前往<a href="http://192.168.1.123:9090/"
                        target="_blank" style="color:#409eff;">http://192.168.1.123:9090/</a> 对应的控制台查看）</p> -->
            </div>
            <el-form :inline="true" :model="form" size="small" class="backup-form">
                <el-form-item label="目标服务器">
                    <el-select v-model="form.serverId" placeholder="请选择服务器" style="width:240px"
                        :loading="serverLoading">
                        <el-option v-for="item in serverList" :key="item.id" :label="item.name" :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="操作类型">
                    <el-select v-model="form.action" placeholder="请选择操作" style="width:240px">
                        <el-option label="启动/重启服务器" value="start"></el-option>
                        <el-option label="关闭服务器" value="stop"></el-option>
                        <el-option label="一键更新服务器代码或配置" value="update"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-checkbox v-model="form.confirm">确认执行服务器操作，业务会中断</el-checkbox>
                </el-form-item>
                <el-form-item>
                    <el-button type="danger" icon="fa fa-power-off" :loading="submitLoading"
                        @click="handleCreatePowerTask" :disabled="!form.confirm || !form.serverId || !form.action">
                        立即执行
                    </el-button>
                </el-form-item>
            </el-form>
        </el-card>

        <!-- 仅保留【当前任务】，删除历史记录 -->
        <el-card v-if="currentTask" shadow="hover" class="task-card">
            <div slot="header">任务执行进度</div>
            <div class="task-row">
                <span>TaskId：{{ currentTask.taskId }}</span>
                <!-- <el-tag :type="statusTagType(currentTask.status)">{{ currentTask.statusText }}</el-tag> -->
            </div>
            <el-progress :percentage="currentTask.progress" :status="progressStatus(currentTask.status)"></el-progress>
            <div style="margin-top:10px; color:#666;" v-html="'消息：' + currentTask.message"></div>
        </el-card>
    </div>
</template>
<script>
function parseNumber(value) {
    var num = Number(value)
    return isNaN(num) ? 0 : num
}
export default {
    data() {
        return {
            form: {
                serverId: "",
                action: "",
                confirm: false
            },
            submitLoading: false,
            serverLoading: false,
            serverList: [],
            currentTask: null,
            pollTimer: null,
            pollingTaskId: null // 记录当前正在轮询的taskId
        }
    },
    methods: {
        isSuccessResponse(res) {
            return !!res && (res.code === 0 || res.code === "0" || res.code == null || res.ok === true || res.success === true)
        },
        responseMessage(res, fallback) {
            if (!res) {
                return fallback || "操作失败"
            }
            return res.desc || res.msg || res.message || fallback || "操作失败"
        },
        fetchServerList() {
            this.serverLoading = true
            this.$api.server.loadServerIds().then((res) => {
                this.serverList = res.ids;
            }).catch(() => {
                this.$message.error('加载服务器列表失败')
            }).finally(() => {
                this.serverLoading = false
            })
        },
        pollTaskStatus(taskId) {
            if (!taskId) {
                this.stopPoll()
                return
            }
            this.$api.yunwei.queryTaskDetail(taskId).then(res => {
                if (!this.isSuccessResponse(res)) {
                    this.$message.error("查询任务状态失败：" + this.responseMessage(res))
                    this.stopPoll()
                    return
                }
                const taskData = res.data
                if (!taskData) {
                    this.$message.error("任务不存在")
                    this.stopPoll()
                    return
                }
                this.currentTask = taskData
                // 任务结束停止轮询
                if (["success", "fail"].includes(taskData.status.toLowerCase())) {
                    this.stopPoll()
                }
            }).catch(err => {
                console.error("轮询请求异常", err)
                this.stopPoll()
            })
        },
        startPoll(taskId) {
            this.stopPoll()
            this.pollingTaskId = taskId
            // 先立即执行一次查询，不用等2秒
            this.pollTaskStatus(taskId)
            this.pollTimer = setInterval(() => {
                this.pollTaskStatus(this.pollingTaskId)
            }, 2000)
        },
        stopPoll() {
            if (this.pollTimer) {
                clearInterval(this.pollTimer)
                this.pollTimer = null
            }
            this.pollingTaskId = null
        },
        statusTagType(status) {
            if (status === "success") return "success"
            if (status === "fail") return "danger"
            return "warning"
        },
        progressStatus(status) {
            if (status === "success") return "success"
            if (status === "fail") return "exception"
            return null
        },
        handleCreatePowerTask() {
            this.submitLoading = true
            var params = {
                serverId: parseNumber(this.form.serverId),
                action: this.form.action
            }
            var payload = {
                taskParam: JSON.stringify(params),
                taskType: "SERVER_POWER"
            }
            this.$api.yunwei.createBackupTask(payload).then(res => {
                if (!this.isSuccessResponse(res)) {
                    this.$message({ type: "error", message: this.responseMessage(res, "创建操作任务失败") })
                    return
                }
                this.$message({ type: "success", message: "任务提交成功" })
                // 优先从 res.data 取taskId
                let taskId = null
                if (res.data && res.data.taskId) {
                    taskId = res.data.taskId
                } else if (res.taskId) {
                    taskId = res.taskId
                }
                if (!taskId) {
                    this.$message.error("未获取到taskId，无法轮询任务进度")
                    return
                }
                this.currentTask = {
                    taskId: taskId,
                    progress: 0,
                    status: "running",
                    statusText: "执行中",
                    message: "任务已下发，等待agent执行"
                }
                this.startPoll(taskId)
                // 重置表单
                this.form = {
                    serverId: "",
                    action: "",
                    confirm: false
                }
            }).catch((err) => {
                console.error("创建操作任务失败", err)
                this.$message({ type: "error", message: "创建操作任务失败" })
            }).finally(() => {
                this.submitLoading = false
            })
        }
    },
    beforeDestroy() {
        this.stopPoll()
    },
    mounted() {
        this.fetchServerList()
    }
}
</script>

<style scoped>
.backup-page {
    padding: 15px;
    background: #f5f7fa;
    min-height: calc(100vh - 84px);
}

.top-panel {
    margin-bottom: 16px;
}

.panel-header {
    font-size: 18px;
    font-weight: 600;
    color: #303133;
}

.backup-form {
    margin-bottom: 8px;
}

.task-card {
    margin-top: 16px;
}

.task-row {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;
}
</style>
