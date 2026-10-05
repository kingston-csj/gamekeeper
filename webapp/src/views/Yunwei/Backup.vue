<template>
    <div class="backup-page">
        <el-card shadow="hover" class="top-panel">
            <div slot="header" class="panel-header">
                <span>数据库备份</span>
            </div>

            <el-form :inline="true" :model="form" size="small" class="backup-form">
                <el-form-item label="服务器id">
                    <el-input v-model.trim="form.serverId" clearable placeholder="请输入整数服务器id" style="width: 220px;" />
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model.trim="form.remark" clearable placeholder="可空，记录本次备份说明" style="width: 320px;" />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="fa fa-play" :loading="submitLoading" @click="handleCreateBackup">
                        立即开始备份
                    </el-button>
                </el-form-item>
            </el-form>

        </el-card>

        <div class="section-title">当前备份任务</div>

        <div v-loading="currentLoading" class="task-list">
            <div v-if="!currentTask" class="empty-panel">
                当前没有备份任务
            </div>

            <el-card v-else :key="currentTask.taskId" shadow="hover" class="task-card">
                <div class="task-card-top">
                    <div class="task-meta">
                        <div class="task-title">
                            <span class="task-id">TaskId: {{ currentTask.taskId }}</span>
                            <el-tag size="small" :type="currentTask.statusType">{{ currentTask.statusText }}</el-tag>
                        </div>
                        <div class="task-sub">
                            <span>服务器id：{{ currentTask.serverId || "-" }}</span>
                            <span v-if="currentTask.remark">备注：{{ currentTask.remark }}</span>
                            <span>耗时：{{ currentTask.elapsedText }}</span>
                        </div>
                    </div>
                    <div class="task-actions">
                        <button v-if="currentTask.status === 'success' && canDownload(currentTask)" type="button"
                            class="link-button" @click="handleDownload(currentTask)">
                            下载备份
                        </button>
                        <button v-if="currentTask.status === 'failed'" type="button" class="link-button danger-link"
                            @click="openFailureLog(currentTask)">
                            查看日志
                        </button>
                    </div>
                </div>

                <el-progress :percentage="displayProgress(currentTask)" :status="progressStatus(currentTask)"
                    :stroke-width="14" />

                <div class="task-log-wrap">
                    <div class="task-log-header">
                        <span>实时日志</span>
                        <span class="task-log-time">{{ formatDateTime(currentTask.updateTime || currentTask.endTime ||
                            currentTask.startTime)
                        }}</span>
                    </div>
                    <div class="task-log-box">
                        <div v-for="(line, index) in visibleLogs(currentTask)" :key="currentTask.taskId + '-' + index"
                            class="task-log-line">
                            {{ line }}
                        </div>
                        <div v-if="!visibleLogs(currentTask).length" class="task-log-line is-placeholder">
                            暂无日志
                        </div>
                    </div>
                </div>
            </el-card>
        </div>

        <el-collapse v-model="historyCollapse" class="history-collapse" @change="handleHistoryCollapse">
            <el-collapse-item name="history">
                <template slot="title">
                    <span class="collapse-title">历史备份（{{ historyList.length }}）</span>
                </template>

                <div class="history-wrap" v-loading="historyLoading">
                    <el-table :data="historyList" border size="mini" style="width: 100%;">
                        <el-table-column prop="taskId" label="TaskId" min-width="180" show-overflow-tooltip />
                        <el-table-column prop="serverId" label="服务器id" width="110" header-align="center"
                            align="center" />
                        <el-table-column prop="remark" label="备注" min-width="180" show-overflow-tooltip>
                            <template slot-scope="scope">
                                {{ scope.row.remark || "-" }}
                            </template>
                        </el-table-column>
                        <el-table-column label="状态" width="100" header-align="center" align="center">
                            <template slot-scope="scope">
                                <el-tag size="mini" :type="scope.row.statusType">{{ scope.row.statusText }}</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="备份时间" min-width="170">
                            <template slot-scope="scope">
                                {{ formatDateTime(scope.row.startTime || scope.row.createTime) }}
                            </template>
                        </el-table-column>
                        <el-table-column label="耗时" width="120" header-align="center" align="center">
                            <template slot-scope="scope">
                                {{ scope.row.elapsedText }}
                            </template>
                        </el-table-column>
                        <el-table-column label="资源状态" width="120" header-align="center" align="center">
                            <template slot-scope="scope">
                                <el-tag v-if="scope.row.resourceDeleted" size="mini" type="info">资源已删除</el-tag>
                                <span v-else class="resource-active">正常</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="180" fixed="right" header-align="center" align="center">
                            <template slot-scope="scope">
                                <el-button v-if="!scope.row.resourceDeleted && canDownload(scope.row)" type="text"
                                    @click="handleDownload(scope.row)">
                                    下载
                                </el-button>
                                <el-button type="text"
                                    :disabled="scope.row.resourceDeleted || deleteLoadingMap[scope.row.rowKey]"
                                    @click="handleDeleteResource(scope.row)">
                                    删除
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>

                    <div v-if="!historyList.length && !historyLoading" class="history-empty">
                        暂无历史备份记录
                    </div>
                </div>
            </el-collapse-item>
        </el-collapse>

        <el-dialog title="失败日志" :visible.sync="logDialogVisible" width="720px">
            <div class="dialog-meta">
                <span>TaskId: {{ activeLogTask.taskId || "-" }}</span>
                <span>服务器id: {{ activeLogTask.serverId || "-" }}</span>
            </div>
            <pre class="dialog-log">{{ activeLogText || "暂无日志" }}</pre>
        </el-dialog>
    </div>
</template>

<script>
function parseNumber(value) {
    var num = Number(value)
    return isNaN(num) ? 0 : num
}

function parseTime(value) {
    if (value == null || value === "") {
        return 0
    }
    if (typeof value === "number") {
        return value
    }
    if (/^\d+$/.test(String(value))) {
        return Number(value)
    }
    var time = new Date(value).getTime()
    return isNaN(time) ? 0 : time
}

function pad(value) {
    return value < 10 ? "0" + value : "" + value
}

function parseBoolean(value) {
    if (value === true || value === false) {
        return value
    }
    if (value == null || value === "") {
        return false
    }
    var text = String(value).toLowerCase()
    return text === "true" || text === "1" || text === "yes"
}

export default {
    data() {
        return {
            form: {
                serverId: "",
                remark: ""
            },
            currentTaskId: "",
            currentTask: null,
            submitLoading: false,
            currentLoading: false,
            historyLoading: false,
            historyList: [],
            historyCollapse: [],
            pollTimer: null,
            currentTaskStatus: "",
            taskStatusReady: false,
            deleteLoadingMap: {},
            logDialogVisible: false,
            activeLogTask: {},
            activeLogText: ""
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
        extractList(res) {
            var data = res && res.data != null ? res.data : res
            if (Array.isArray(data)) {
                return data
            }
            if (!data || typeof data !== "object") {
                return []
            }
            var keys = ["list", "content", "records", "rows", "items", "tasks", "history"]
            for (var i = 0; i < keys.length; i++) {
                if (Array.isArray(data[keys[i]])) {
                    return data[keys[i]]
                }
            }
            return []
        },
        extractRecord(res) {
            var data = res && res.data != null ? res.data : res
            if (!data || Array.isArray(data) || typeof data !== "object") {
                return null
            }
            return data
        },
        pickValue(source, keys, fallback) {
            if (!source) {
                return fallback
            }
            for (var i = 0; i < keys.length; i++) {
                var key = keys[i]
                if (source[key] !== undefined && source[key] !== null && source[key] !== "") {
                    return source[key]
                }
            }
            return fallback
        },
        normalizeStatus(item) {
            var raw = this.pickValue(item, ["status", "state", "taskStatus"], "")
            var text = String(raw || "").toLowerCase()
            var progress = parseNumber(this.pickValue(item, ["progress", "percent", "percentage"], 0))
            if (progress < 0 || text.indexOf("fail") >= 0 || text.indexOf("error") >= 0 || text.indexOf("exception") >= 0 || text.indexOf("失败") >= 0) {
                return { value: "failed", text: "失败", type: "danger" }
            }
            if (text.indexOf("success") >= 0 || text.indexOf("done") >= 0 || text.indexOf("finish") >= 0 || text.indexOf("成功") >= 0 || raw === 1 || raw === "1" || progress >= 100) {
                return { value: "success", text: "成功", type: "success" }
            }
            return { value: "running", text: "进行中", type: "warning" }
        },
        normalizeLogs(item) {
            var logs = []
            var append = function (value) {
                if (value == null || value === "") {
                    return
                }
                var text = String(value)
                if (!logs.length || logs[logs.length - 1] !== text) {
                    logs.push(text)
                }
            }
            var appendArray = function (arr) {
                if (!Array.isArray(arr)) {
                    return
                }
                for (var i = 0; i < arr.length; i++) {
                    append(arr[i])
                }
            }

            appendArray(item.logList)
            appendArray(item.logs)
            appendArray(item.messages)

            append(this.pickValue(item, ["latestLog", "log", "message", "errorMessage"], ""))
            return logs
        },
        normalizeRecord(item, index, isHistory) {
            var statusInfo = this.normalizeStatus(item)
            var startTime = parseTime(this.pickValue(item, ["startTime", "createTime", "createAt", "gmtCreate"], 0))
            var endTime = parseTime(this.pickValue(item, ["endTime", "finishTime", "updateTime", "completeTime"], 0))
            var updateTime = parseTime(this.pickValue(item, ["updateTime", "endTime", "finishTime", "gmtModified"], 0))
            var resourceDeleted = parseBoolean(this.pickValue(item, ["resourceDeleted", "deleted", "fileDeleted", "minioDeleted"], false))
            var record = {
                rowKey: this.pickValue(item, ["id", "taskId"], "row-" + index),
                id: this.pickValue(item, ["id", "historyId"], ""),
                taskId: this.pickValue(item, ["taskId"], ""),
                url: this.pickValue(item, ["url"], ""),
                rawStatus: this.pickValue(item, ["status", "state", "taskStatus"], ""),
                serverId: this.pickValue(item, ["serverId", "sid"], ""),
                remark: this.pickValue(item, ["remark", "note"], ""),
                status: statusInfo.value,
                statusText: statusInfo.text,
                statusType: statusInfo.type,
                progress: parseNumber(this.pickValue(item, ["progress", "percent", "percentage"], 0)),
                startTime: startTime,
                endTime: endTime,
                updateTime: updateTime,
                createTime: startTime,
                resourceDeleted: resourceDeleted,
                minioPath: this.pickValue(item, ["minioPath", "objectName", "fileKey"], ""),
                fileName: this.pickValue(item, ["fileName", "name"], ""),
                logs: this.normalizeLogs(item),
                raw: item,
                isHistory: !!isHistory
            }
            record.elapsedText = this.formatElapsed(record)
            return record
        },
        sortHistory(list) {
            return list.slice().sort(function (a, b) {
                var bTime = b.startTime || b.createTime || b.endTime || 0
                var aTime = a.startTime || a.createTime || a.endTime || 0
                return bTime - aTime
            })
        },
        validateServerId() {
            if (!/^\d+$/.test(this.form.serverId || "")) {
                this.$message({ type: "warning", message: "服务器id必须为整数" })
                return false
            }
            return true
        },
        createLocalPendingTask(taskId) {
            return this.normalizeRecord({
                taskId: taskId,
                serverId: this.form.serverId,
                remark: this.form.remark,
                status: "PENDING",
                progress: 0,
                message: "备份任务已提交，等待执行",
                startTime: Date.now(),
                updateTime: Date.now()
            }, Date.now(), false)
        },
        handleCreateBackup() {
            if (!this.validateServerId()) {
                return
            }
            var params = {
                serverId: parseNumber(this.form.serverId),
            }
            var payload = {
                taskParam: JSON.stringify(params),
                taskType: "DB_BACKUP",
                remark: this.form.remark
            }
            this.submitLoading = true
            this.$api.yunwei.createBackupTask(payload).then(res => {
                if (!this.isSuccessResponse(res)) {
                    this.$message({ type: "error", message: this.responseMessage(res, "创建备份任务失败") })
                    return
                }
                var taskId = this.pickValue(res, ["taskId"], "")
                if (!taskId && res.data) {
                    taskId = this.pickValue(res.data, ["taskId", "id"], "")
                }
                if (taskId) {
                    this.currentTaskId = taskId
                    this.currentTask = this.createLocalPendingTask(taskId)
                    this.currentTaskStatus = this.currentTask.status
                    this.taskStatusReady = true
                    this.fetchCurrentTask({ silent: true })
                    this.startPolling()
                }
                this.$message({ type: "success", message: "备份任务已创建" })
            }).catch(() => {
                this.$message({ type: "error", message: "创建备份任务失败" })
            }).finally(() => {
                this.submitLoading = false
            })
        },
        syncTaskStatus(task, options) {
            var nextStatus = task ? task.status : ""
            if (!this.taskStatusReady || options && options.resetSnapshot) {
                this.currentTaskStatus = nextStatus
                this.taskStatusReady = true
                return
            }
            if (task && this.currentTaskStatus && this.currentTaskStatus !== task.status) {
                this.notifyTaskStatusChange(task)
            }
            this.currentTaskStatus = nextStatus
        },
        notifyTaskStatusChange(task) {
            var type = "info"
            if (task.status === "success") {
                type = "success"
            } else if (task.status === "failed") {
                type = "error"
            }
            this.$message({
                type: type,
                message: "任务 " + task.taskId + " 状态更新为" + task.statusText
            })
        },
        fetchCurrentTask(options) {
            var requestOptions = options || {}
            if (!requestOptions.silent) {
                this.currentLoading = true
            }
            if (!this.currentTaskId) {
                this.currentTask = null
                this.currentTaskStatus = ""
                this.taskStatusReady = false
                this.currentLoading = false
                return
            }
            this.$api.yunwei.queryTaskDetail(this.currentTaskId).then(res => {
                if (!this.isSuccessResponse(res)) {
                    if (!requestOptions.silent) {
                        this.$message({ type: "error", message: this.responseMessage(res, "加载当前任务失败") })
                    }
                    return
                }
                var record = this.extractRecord(res)
                if (!record) {
                    this.currentTask = null
                    this.currentTaskId = ""
                    this.currentTaskStatus = ""
                    this.taskStatusReady = false
                    this.stopPolling()
                    return
                }
                var normalized = this.normalizeRecord(record, 0, false)
                this.syncTaskStatus(normalized, requestOptions)
                this.currentTask = normalized
                this.currentTaskId = normalized.taskId || this.currentTaskId
                if (normalized.status === "running") {
                    this.startPolling()
                } else {
                    this.stopPolling()
                    this.fetchHistory({ silent: true })
                }
            }).catch(() => {
                if (!requestOptions.silent) {
                    this.$message({ type: "error", message: "加载当前任务失败" })
                }
            }).finally(() => {
                this.currentLoading = false
            })
        },
        fetchHistory(options) {
            var requestOptions = options || {}
            if (!requestOptions.silent) {
                this.historyLoading = true
            }
            this.$api.yunwei.queryBackupHistory({
                taskType: "DB_BACKUP",
                pageNum: 1,
                pageSize: 100
            }).then(res => {
                if (!this.isSuccessResponse(res)) {
                    if (!requestOptions.silent) {
                        this.$message({ type: "error", message: this.responseMessage(res, "加载历史备份失败") })
                    }
                    return
                }
                var list = this.extractList(res)
                    .map((item, index) => this.normalizeRecord(item, index, true))
                    .filter(item => item.status === "success" || item.status === "failed")
                this.historyList = this.sortHistory(list)
            }).catch(() => {
                if (!requestOptions.silent) {
                    this.$message({ type: "error", message: "加载历史备份失败" })
                }
            }).finally(() => {
                this.historyLoading = false
            })
        },
        startPolling() {
            if (this.pollTimer) {
                return
            }
            var self = this
            this.pollTimer = setInterval(function () {
                self.fetchCurrentTask({ silent: true })
            }, 1000)
        },
        stopPolling() {
            if (!this.pollTimer) {
                return
            }
            clearInterval(this.pollTimer)
            this.pollTimer = null
        },
        handleHistoryCollapse(value) {
            if (value.indexOf("history") >= 0 && !this.historyList.length) {
                this.fetchHistory()
            }
        },
        progressStatus(task) {
            if (task.status === "success") {
                return "success"
            }
            if (task.status === "failed") {
                return "exception"
            }
            return ""
        },
        displayProgress(task) {
            if (task.status === "success" || task.status === "failed") {
                var finishedProgress = parseNumber(task.progress)
                if (finishedProgress <= 0) {
                    finishedProgress = 100
                }
                return Math.max(0, Math.min(100, finishedProgress))
            }
            return Math.max(0, Math.min(99, parseNumber(task.progress)))
        },
        visibleLogs(task) {
            var logs = task && task.logs ? task.logs : []
            if (logs.length <= 12) {
                return logs
            }
            return logs.slice(logs.length - 12)
        },
        formatDateTime(value) {
            var time = parseTime(value)
            if (!time) {
                return "-"
            }
            var date = new Date(time)
            return date.getFullYear() + "-" + pad(date.getMonth() + 1) + "-" + pad(date.getDate()) +
                " " + pad(date.getHours()) + ":" + pad(date.getMinutes()) + ":" + pad(date.getSeconds())
        },
        formatElapsed(record) {
            if (!record) {
                return "-"
            }
            var start = record.startTime || record.createTime
            if (!start) {
                return "-"
            }
            var end = record.status === "running" ? Date.now() : (record.endTime || record.updateTime || Date.now())
            var diff = Math.max(0, end - start)
            var totalSeconds = Math.floor(diff / 1000)
            var hours = Math.floor(totalSeconds / 3600)
            var minutes = Math.floor((totalSeconds % 3600) / 60)
            var seconds = totalSeconds % 60
            if (hours > 0) {
                return hours + "h " + minutes + "m " + seconds + "s"
            }
            if (minutes > 0) {
                return minutes + "m " + seconds + "s"
            }
            return seconds + "s"
        },
        canDownload(record) {
            if (!record || record.resourceDeleted) {
                return false
            }
            if (record.status !== "success") {
                return false
            }
            return !!record.taskId
        },
        resolveDownloadFileName(record) {
            var url = record && record.url ? String(record.url) : ""
            if (url) {
                var marker = "backup/"
                var index = url.indexOf(marker)
                var fileName = index >= 0 ? url.substring(index + marker.length) : url.split(/[\\/]/).pop()
                fileName = fileName ? fileName.split("?")[0].split("#")[0] : ""
                if (fileName) {
                    return fileName
                }
            }
            return record && record.taskId ? record.taskId + ".gz" : "backup_" + Date.now() + ".gz"
        },
        handleDownload(record) {
            if (!this.canDownload(record)) {
                this.$message({ type: "warning", message: "当前备份不可下载" })
                return
            }
            this.$api.yunwei.downloadBackupFile(record.taskId).then(res => {
                var blob = res instanceof Blob ? res : new Blob([res])
                var fileName = this.resolveDownloadFileName(record)
                var downloadUrl = window.URL.createObjectURL(blob)
                var link = document.createElement("a")
                link.href = downloadUrl
                link.download = fileName
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link)
                window.URL.revokeObjectURL(downloadUrl)
            }).catch(() => {
                this.$message({ type: "error", message: "下载备份失败" })
            })
        },
        openFailureLog(task) {
            this.activeLogTask = task || {}
            this.activeLogText = (task && task.logs && task.logs.length) ? task.logs.join("\n") : (this.pickValue(task, ["message"], "") || "")
            this.logDialogVisible = true
        },
        handleDeleteResource(row) {
            var rowKey = row.rowKey
            this.$confirm("确认删除该备份在 MinIO 上的资源吗？数据库中的历史记录会保留，不会删除。", "提示", {
                type: "warning"
            }).then(() => {
                this.$set(this.deleteLoadingMap, rowKey, true)
                this.$api.yunwei.deleteBackupResource({
                    id: row.id,
                    taskId: row.taskId,
                    serverId: row.serverId,
                    minioPath: row.minioPath
                }).then(res => {
                    if (!this.isSuccessResponse(res)) {
                        this.$message({ type: "error", message: this.responseMessage(res, "删除资源失败") })
                        return
                    }
                    row.resourceDeleted = true
                    this.$message({ type: "success", message: "资源删除成功，历史记录已保留" })
                }).catch(() => {
                    this.$message({ type: "error", message: "删除资源失败" })
                }).finally(() => {
                    this.$set(this.deleteLoadingMap, rowKey, false)
                })
            }).catch(() => { })
        }
    },
    mounted() {
        this.fetchHistory({ silent: true })
    },
    beforeDestroy() {
        this.stopPolling()
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

.section-title {
    margin: 6px 0 12px 2px;
    font-size: 16px;
    font-weight: 600;
    color: #303133;
}

.task-list {
    margin-bottom: 16px;
}

.empty-panel {
    padding: 36px 20px;
    text-align: center;
    color: #909399;
    background: #fff;
    border-radius: 6px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.task-card {
    margin-bottom: 14px;
    border-radius: 8px;
}

.task-card-top {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 12px;
    margin-bottom: 14px;
}

.task-title {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 10px;
    flex-wrap: wrap;
}

.task-id {
    font-size: 15px;
    font-weight: 600;
    color: #303133;
}

.task-sub {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    color: #606266;
    font-size: 13px;
}

.task-actions {
    flex-shrink: 0;
    padding-top: 2px;
}

.link-button {
    padding: 0;
    border: none;
    background: transparent;
    color: #409eff;
    cursor: pointer;
    font-size: 13px;
}

.link-button:hover {
    color: #66b1ff;
}

.danger-link {
    color: #f56c6c;
}

.danger-link:hover {
    color: #f78989;
}

.task-log-wrap {
    margin-top: 14px;
}

.task-log-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 8px;
    color: #606266;
    font-size: 13px;
}

.task-log-time {
    color: #909399;
}

.task-log-box {
    max-height: 180px;
    overflow-y: auto;
    padding: 10px 12px;
    border-radius: 6px;
    background: #0f172a;
    color: #dbeafe;
    font-size: 12px;
    line-height: 1.6;
    font-family: Consolas, "Courier New", monospace;
}

.task-log-line {
    white-space: pre-wrap;
    word-break: break-all;
}

.task-log-line.is-placeholder {
    color: #94a3b8;
}

.history-collapse {
    background: transparent;
    border: none;
}

.collapse-title {
    font-size: 15px;
    font-weight: 600;
    color: #303133;
}

.history-wrap {
    background: #fff;
    border-radius: 6px;
}

.history-empty {
    padding: 20px;
    text-align: center;
    color: #909399;
}

.resource-active {
    color: #67c23a;
}

.dialog-meta {
    margin-bottom: 12px;
    color: #606266;
    font-size: 13px;
    display: flex;
    gap: 16px;
    flex-wrap: wrap;
}

.dialog-log {
    margin: 0;
    max-height: 420px;
    overflow-y: auto;
    padding: 12px;
    border-radius: 6px;
    background: #0f172a;
    color: #dbeafe;
    font-size: 12px;
    line-height: 1.6;
    white-space: pre-wrap;
    word-break: break-all;
    font-family: Consolas, "Courier New", monospace;
}
</style>
