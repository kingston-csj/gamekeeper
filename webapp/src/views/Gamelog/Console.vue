<template>
    <div class="terminal-log-container">
        <el-card shadow="hover" class="top-panel">
            <div slot="header" class="panel-header">
                <span>终端日志</span>
            </div>
            <el-form :inline="true" :model="form" size="small">
                <el-form-item label="目标服务器">
                    <el-select v-model="form.serverId" placeholder="请选择服务器" style="width:240px" :loading="serverLoading"
                        @change="handleServerChange">
                        <el-option v-for="item in serverList" :key="item.id" :label="item.name"
                            :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
        </el-card>

        <div class="iframe-wrap" style="margin-top:10px;">
            <iframe v-if="form.serverId" :src="iframeUrl" frameborder="0"
                style="width:100%;height:calc(100vh - 200px)"></iframe>
            <el-empty v-else description="请先选择服务器"></el-empty>
        </div>
    </div>
</template>

<script>
import { baseUrl } from "@/utils/global";

function parseNumber(value) {
    var num = Number(value)
    return isNaN(num) ? 0 : num
}
export default {
    name: "TerminalLog",
    data() {
        return {
            form: {
                serverId: ""
            },
            serverLoading: false,
            serverList: [],
            iframeUrl: ""
        }
    },
    methods: {
        fetchServerList() {
            this.serverLoading = true
            this.$api.server.loadServerIds().then((res) => {
                this.serverList = res.ids;
                // 路由query回显，页面刷新保留选中serverId
                const queryServerId = this.$route.query.serverId
                if (queryServerId) {
                    this.form.serverId = parseNumber(queryServerId)
                    this.handleServerChange()
                }
            }).catch(() => {
                this.$message.error('加载服务器列表失败')
            }).finally(() => {
                this.serverLoading = false
            })
        },
        handleServerChange() {
            // 必须用后台绝对地址，否则相对路径会被前端路由吃掉，iframe里渲染出整个站点
            this.iframeUrl = `${baseUrl}/gamelog/${this.form.serverId}/log/`
            // 更新路由query，刷新页面保留选择
            this.$router.replace({
                query: {
                    serverId: this.form.serverId
                }
            })
        }
    },
    mounted() {
        this.fetchServerList()
    }
}
</script>

<style scoped>
.terminal-log-container {
    padding: 15px;
}
</style>
