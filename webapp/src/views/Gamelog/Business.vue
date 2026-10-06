<template>
    <div>
        <div style="padding: 15px 15px 0 15px;">
            <el-form :inline="true" :model="filters" size="small">
                <el-form-item label="服务器">
                    <el-select v-model="filters.sid" @change="handleServerChange" clearable placeholder="请选择服务器"
                        :loading="loading" style="width: 200px;">
                        <el-option v-for="item in servers" :key="item.id" :label="item.name" :value="String(item.id)">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="时间范围">
                    <el-date-picker v-model="filters.timeRange" type="datetimerange" range-separator="至"
                        start-placeholder="开始时间" end-placeholder="结束时间" style="width: 380px;">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="模块">
                    <el-select v-model="filters.module" clearable filterable placeholder="请选择模块" style="width: 160px;">
                        <el-option v-for="item in logTypes" :key="item" :label="item" :value="item">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="玩家ID">
                    <el-input v-model.trim="filters.playerId" placeholder="请输入玩家ID" style="width: 180px;">
                    </el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="handleSearch">查询</el-button>
                    <el-button @click="handleReset">重置</el-button>
                    <el-button type="success" plain @click="addExtFilter">新增 ext 筛选</el-button>
                </el-form-item>
            </el-form>
        </div>

        <div v-if="extFilters.length > 0" style="padding: 0 15px 10px 15px;">
            <div v-for="(item, index) in extFilters" :key="item.id" style="margin-top: 10px;">
                <el-form :inline="true" size="small">
                    <el-form-item>
                        <el-select v-model="item.field" filterable clearable placeholder="选择 ext 字段"
                            style="width: 180px;">
                            <el-option v-for="field in extFieldOptions" :key="field" :label="field" :value="field">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-select v-model="item.operator" placeholder="运算符" style="width: 140px;">
                            <el-option v-for="operator in operatorOptions" :key="operator.value" :label="operator.label"
                                :value="operator.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item v-if="item.operator === 'between'">
                        <el-input v-model.trim="item.rangeValue"
                            @input="item.rangeValue = normalizeCommaValue(item.rangeValue)" placeholder="最小值,最大值"
                            style="width: 220px;">
                        </el-input>
                    </el-form-item>
                    <el-form-item v-else-if="item.operator === 'in'">
                        <el-input v-model.trim="item.multiValue"
                            @input="item.multiValue = normalizeCommaValue(item.multiValue)" placeholder="多个值用逗号分隔"
                            style="width: 220px;">
                        </el-input>
                    </el-form-item>
                    <el-form-item v-else>
                        <el-input v-model.trim="item.value" placeholder="请输入筛选值" style="width: 220px;">
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="danger" plain @click="removeExtFilter(index)">X</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>

        <div style="padding: 15px;">
            <el-table :data="tableData" border v-loading="loading" style="width: 100%">
                <el-table-column prop="time" label="时间" min-width="180">
                    <template slot-scope="scope">
                        {{ formatTime(scope.row.time) }}
                    </template>
                </el-table-column>
                <el-table-column prop="module" label="模块" min-width="100">
                </el-table-column>
                <el-table-column prop="playerId" label="玩家ID" min-width="140">
                </el-table-column>
                <el-table-column prop="sid" label="服务器" min-width="100">
                </el-table-column>
                <el-table-column prop="reason" label="原因" min-width="120">
                </el-table-column>
                <el-table-column v-for="key in dynamicExtKeys" :key="key" :label="key" min-width="160">
                    <template slot-scope="scope">
                        {{ formatExtValue(scope.row.ext && scope.row.ext[key]) }}
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

function createExtFilter() {
    return {
        id: Date.now() + Math.random(),
        field: "",
        operator: "=",
        value: "",
        rangeValue: "",
        multiValue: ""
    };
}

export default {
    data() {
        return {
            loading: false,
            servers: [],
            logTypes: [],
            tableData: [],
            total: 0,
            dynamicExtKeys: [],
            extFieldOptions: [],
            extFilters: [],
            operatorOptions: [
                { label: "等于", value: "=" },
                { label: "不等于", value: "!=" },
                { label: "大于", value: ">" },
                { label: "大于等于", value: ">=" },
                { label: "小于", value: "<" },
                { label: "小于等于", value: "<=" },
                { label: "包含", value: "like" },
                { label: "区间", value: "between" },
                { label: "多选", value: "in" }
            ],
            filters: {
                sid: "",
                timeRange: createDefaultTimeRange(),
                module: "",
                date: "",
                playerId: ""
            },
            pageRequest: {
                pageNum: 1,
                pageSize: 10
            },
            totalPages: 0
        };
    },
    methods: {
        searchServers() {
            this.$api.server.loadServerIds().then(res => {
                this.servers = res.ids || [];
            });
        },
        loadLogTypes() {
            this.$api.gamelog.queryLogTypes({
                serverId: this.filters.sid || 0
            }).then(res => {
                if (!res || !res.data) {
                    this.logTypes = [];
                    return;
                }
                if (Array.isArray(res.data)) {
                    this.logTypes = res.data;
                    return;
                }
                try {
                    this.logTypes = JSON.parse(res.data) || [];
                } catch (e) {
                    this.logTypes = [];
                }
            });
        },
        getCursorPayload(direction) {
            if (!direction || !this.tableData.length) {
                return {
                    cursorTime: 0,
                    cursorSid: "",
                    cursorPlayerId: "",
                    cursorDirection: ""
                };
            }
            const row = direction === "prev" ? this.tableData[0] : this.tableData[this.tableData.length - 1];
            return {
                cursorTime: row.time || 0,
                cursorSid: row.sid || "",
                cursorPlayerId: row.playerId || "",
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
                module: this.filters.module,
                playerId: this.filters.playerId,
                sid: this.filters.sid,
                filters: this.buildExtFilters(),
                pageNum: requestOptions.pageNum || this.pageRequest.pageNum,
                pageSize: this.pageRequest.pageSize,
                ...this.getCursorPayload(requestOptions.direction)
            };
        },
        findPage(options) {
            const requestOptions = options || {};
            const params = this.getQueryParams(requestOptions);
            this.loading = true;
            this.$api.gamelog
                .queryItemLog(params)
                .then(res => {
                    const data = res.data || {};
                    const content = data.content || [];
                    this.tableData = content;
                    this.total = data.totalSize || 0;
                    this.totalPages = data.totalPages || 0;
                    this.pageRequest.pageNum = params.pageNum;
                    this.dynamicExtKeys = this.collectExtKeys(content);
                    this.mergeExtFieldOptions(this.dynamicExtKeys);
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        collectExtKeys(content) {
            const keys = [];
            const exists = {};
            for (let i = 0; i < content.length; i++) {
                const ext = content[i].ext || {};
                Object.keys(ext).forEach(key => {
                    if (!exists[key]) {
                        exists[key] = true;
                        keys.push(key);
                    }
                });
            }
            return keys;
        },
        mergeExtFieldOptions(keys) {
            const exists = {};
            const all = [];
            this.extFieldOptions.concat(keys || []).forEach(key => {
                if (!key || exists[key]) {
                    return;
                }
                exists[key] = true;
                all.push(key);
            });
            this.extFieldOptions = all;
        },
        buildExtFilters() {
            return this.extFilters
                .filter(item => item.field && this.hasFilterValue(item))
                .map(item => {
                    const rangeValue = this.normalizeCommaValue(item.rangeValue);
                    const multiValue = this.normalizeCommaValue(item.multiValue);
                    return {
                        field: "ext." + item.field,
                        operator: item.operator,
                        value: item.operator === "between" || item.operator === "in" ? "" : item.value,
                        rangeValue: item.operator === "between" ? rangeValue : "",
                        multiValue: item.operator === "in" ? multiValue : ""
                    };
                });
        },
        hasFilterValue(item) {
            if (item.operator === "between") {
                return !!this.normalizeCommaValue(item.rangeValue);
            }
            if (item.operator === "in") {
                return !!this.normalizeCommaValue(item.multiValue);
            }
            return !!item.value;
        },
        normalizeCommaValue(value) {
            if (!value) {
                return "";
            }
            return String(value).replace(/，/g, ",");
        },
        addExtFilter() {
            this.extFilters.push(createExtFilter());
        },
        removeExtFilter(index) {
            this.extFilters.splice(index, 1);
        },
        handleSearch() {
            this.findPage({
                needCount: true,
                pageNum: 1,
                direction: ""
            });
        },
        handleServerChange() {
            this.filters.module = "";
            this.loadLogTypes();
        },
        handleReset() {
            this.filters = {
                sid: "",
                timeRange: createDefaultTimeRange(),
                module: "",
                playerId: ""
            };
            this.extFilters = [];
            this.pageRequest.pageSize = 10;
            this.pageRequest.pageNum = 1;
            this.total = 0;
            this.totalPages = 0;
            this.loadLogTypes();
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
        },
        formatExtValue(value) {
            if (value === null || value === undefined) {
                return "";
            }
            if (typeof value === "object") {
                return JSON.stringify(value);
            }
            return value;
        }
    },
    mounted() {
        this.searchServers();
        this.loadLogTypes();
        this.findPage({
            needCount: true,
            pageNum: 1,
            direction: ""
        });
    }
};
</script>
