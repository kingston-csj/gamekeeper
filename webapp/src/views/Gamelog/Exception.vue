<template>
    <div>
        <div style="padding: 15px 15px 0 15px;">
            <el-form :inline="true" :model="filters" size="small">
                <el-form-item label="服务器">
                    <el-select v-model="filters.sid" clearable placeholder="请选择服务器" :loading="loading"
                        style="width: 200px;">
                        <el-option v-for="item in servers" :key="item.id" :label="item.name" :value="String(item.id)">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="时间范围">
                    <el-date-picker v-model="filters.timeRange" type="datetimerange" range-separator="至"
                        start-placeholder="开始时间" end-placeholder="结束时间" style="width: 380px;">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="关键字">
                    <el-input v-model="filters.keyword" clearable placeholder="空格分隔，例如 a b ~c：包含a和b，排除c"
                        style="width: 320px;">
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleSearch">查询</el-button>
                    <el-button @click="handleReset">重置</el-button>
                </el-form-item>
            </el-form>
        </div>

        <div style="padding: 15px;">
            <el-table :data="tableData" border v-loading="loading" style="width: 100%">
                <el-table-column prop="time" label="时间" min-width="180">
                    <template slot-scope="scope">
                        {{ formatTime(scope.row.time) }}
                    </template>
                </el-table-column>
                <el-table-column prop="serverId" label="服务器" min-width="100">
                </el-table-column>
                <el-table-column prop="message" label="错误消息" min-width="400">
                    <template slot-scope="scope">
                        <div class="exception-message-wrap">
                            <div class="exception-message" :class="{
                                'is-collapsed': overflowRows[scope.$index] && !expandedRows[scope.$index]
                            }">{{ scope.row.message }}</div>
                            <div v-if="overflowRows[scope.$index]" class="exception-toggle-row">
                                <button type="button" class="exception-toggle"
                                    @click.stop="toggleMessage(scope.$index)">
                                    {{ expandedRows[scope.$index] ? '▲' : '▼' }}
                                </button>
                            </div>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div style="padding: 0 15px 15px 15px; text-align: right;">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="pageRequest.pageNum" :page-sizes="[10, 20, 50]" :page-size="pageRequest.pageSize"
                layout="total, sizes, prev, next" :total="total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
function createDefaultTimeRange() {
    const end = new Date();
    const start = new Date(end.getTime() - 24 * 60 * 60 * 1000);
    return [start, end];
}

function toMillis(time) {
    if (!time) {
        return 0;
    }
    if (typeof time === "number") {
        return time;
    }
    return new Date(time).getTime();
}

function pad(value) {
    return value < 10 ? "0" + value : "" + value;
}

export default {
    data() {
        return {
            loading: false,
            servers: [],
            tableData: [],
            total: 0,
            filters: {
                sid: "",
                timeRange: createDefaultTimeRange(),
                keyword: ""
            },
            pageRequest: {
                pageNum: 1,
                pageSize: 10
            },
            totalPages: 0,
            expandedRows: {},
            overflowRows: {}
        };
    },
    methods: {
        buildOverflowRows(content) {
            const overflowMap = {};
            (content || []).forEach((item, index) => {
                const message = item && item.message ? String(item.message) : "";
                const lineCount = message === "" ? 1 : message.split(/\r?\n/).length;
                overflowMap[index] = lineCount > 5;
            });
            return overflowMap;
        },
        searchServers() {
            this.$api.server.loadServerIds().then(res => {
                this.servers = res.ids || [];
            });
        },
        getCursorPayload(direction) {
            if (!direction || !this.tableData.length) {
                return {
                    cursorTime: 0,
                    cursorSid: "",
                    cursorDirection: ""
                };
            }
            const row = direction === "prev" ? this.tableData[0] : this.tableData[this.tableData.length - 1];
            return {
                cursorTime: row.time || 0,
                cursorSid: row.serverId || "",
                cursorDirection: direction
            };
        },
        getQueryParams(options) {
            const timeRange = this.filters.timeRange || [];
            const requestOptions = options || {};
            return {
                needCount: !!requestOptions.needCount,
                cachedTotalSize: this.total,
                cachedTotalPages: this.totalPages,
                startTime: timeRange.length === 2 ? toMillis(timeRange[0]) : 0,
                endTime: timeRange.length === 2 ? toMillis(timeRange[1]) : 0,
                sid: this.filters.sid,
                keyword: this.filters.keyword,
                pageNum: requestOptions.pageNum || this.pageRequest.pageNum,
                pageSize: this.pageRequest.pageSize,
                ...this.getCursorPayload(requestOptions.direction)
            };
        },
        findPage(options) {
            const requestOptions = options || {};
            const params = this.getQueryParams(requestOptions);
            this.expandedRows = {};
            this.overflowRows = {};
            this.loading = true;
            this.$api.gamelog
                .queryExceptionLog(params)
                .then(res => {
                    const data = res.data || {};
                    const content = data.content || [];
                    this.tableData = content;
                    this.overflowRows = this.buildOverflowRows(content);
                    this.total = data.totalSize || 0;
                    this.totalPages = data.totalPages || 0;
                    this.pageRequest.pageNum = params.pageNum;
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        toggleMessage(idx) {
            this.$set(this.expandedRows, idx, !this.expandedRows[idx]);
        },
        handleSearch() {
            this.findPage({
                needCount: true,
                pageNum: 1,
                direction: ""
            });
        },
        handleReset() {
            this.filters = {
                sid: "",
                timeRange: createDefaultTimeRange(),
                keyword: ""
            };
            this.pageRequest.pageSize = 10;
            this.pageRequest.pageNum = 1;
            this.total = 0;
            this.totalPages = 0;
            this.findPage({
                needCount: true,
                pageNum: 1,
                direction: ""
            });
        },
        handleSizeChange(val) {
            this.pageRequest.pageSize = val;
            this.findPage({
                needCount: true,
                pageNum: 1,
                direction: ""
            });
        },
        handleCurrentChange(val) {
            if (val === this.pageRequest.pageNum || !this.tableData.length) {
                return;
            }
            this.findPage({
                needCount: false,
                pageNum: val,
                direction: val > this.pageRequest.pageNum ? "next" : "prev"
            });
        },
        formatTime(value) {
            if (!value) {
                return "";
            }
            const date = new Date(Number(value));
            if (isNaN(date.getTime())) {
                return value;
            }
            return (
                date.getFullYear() +
                "-" +
                pad(date.getMonth() + 1) +
                "-" +
                pad(date.getDate()) +
                " " +
                pad(date.getHours()) +
                ":" +
                pad(date.getMinutes()) +
                ":" +
                pad(date.getSeconds())
            );
        }
    },
    mounted() {
        this.searchServers();
        this.findPage({
            needCount: true,
            pageNum: 1,
            direction: ""
        });
    }
};
</script>

<style scoped>
.exception-message-wrap {
    width: 100%;
}

.exception-message {
    white-space: pre-wrap;
    word-break: break-all;
    line-height: 20px;
}

.exception-message.is-collapsed {
    max-height: 100px;
    overflow: hidden;
}

.exception-toggle-row {
    text-align: right;
    margin-top: 4px;
}

.exception-toggle {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    color: #409EFF;
    font-size: 14px;
    font-weight: 700;
    user-select: none;
    z-index: 10;
    padding: 0 8px;
    border-radius: 2px;
    line-height: 20px;
    height: 20px;
    min-width: 32px;
    background: transparent;
    box-sizing: border-box;
    border: none;
    outline: none;
}

.exception-toggle:hover {
    background: #ecf5ff;
}
</style>
