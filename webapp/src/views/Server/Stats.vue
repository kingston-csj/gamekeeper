<template>
    <div class="page-container">
        <div class="toolbar" style="padding:15px;">
            <el-form :inline="true" :model="filters" size="small">
                <el-form-item label="服务器">
                    <el-select v-model="filters.serverId" placeholder="请选择服务器" style="width:200px;">
                        <el-option v-for="item in servers" :key="item.id" :label="item.name" :value="String(item.id)">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="时间范围">
                    <el-date-picker v-model="filters.dateRange" type="datetimerange" range-separator="至"
                        start-placeholder="开始时间" end-placeholder="结束时间" value-format="yyyy-MM-dd HH:mm:ss"
                        format="yyyy-MM-dd HH:mm:ss" style="width:380px;">
                    </el-date-picker>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="fa fa-search" @click="handleSearch">查询</el-button>
                    <el-button icon="fa fa-refresh" @click="handleReset">重置</el-button>
                </el-form-item>
            </el-form>
        </div>

        <div style="padding:0 15px 15px 15px;">
            <el-row :gutter="16">
                <el-col :span="4">
                    <el-card shadow="hover" class="metric-card" :class="{ 'danger-card': isDanger('heapUsageRate') }">
                        <div class="metric-title">堆使用 / 最大堆 (MB)</div>
                        <div class="metric-value">
                            <span>{{ formatMB(latest.heapUsed) }}</span>
                            <span class="metric-sub"> / {{ formatMB(latest.heapMax) }}</span>
                        </div>
                        <div class="metric-rate">
                            <span :class="dangerClass('heapUsageRate')">使用率 {{ toFixed(latest.heapUsageRate) }}%</span>
                        </div>
                    </el-card>
                </el-col>
                <el-col :span="4">
                    <el-card shadow="hover" class="metric-card" :class="{ 'danger-card': isDanger('directUsed') }">
                        <div class="metric-title">直接内存 (MB)</div>
                        <div class="metric-value">{{ formatMB(latest.directUsed) }}</div>
                        <div class="metric-rate">&nbsp;</div>
                    </el-card>
                </el-col>
                <el-col :span="4">
                    <el-card shadow="hover" class="metric-card"
                        :class="{ 'danger-card': isDanger('processCpuPercent') }">
                        <div class="metric-title">进程CPU</div>
                        <div class="metric-value">
                            <span :class="dangerClass('processCpuPercent')">{{ toFixed(latest.processCpuPercent)
                            }}%</span>
                        </div>
                        <div class="metric-rate">&nbsp;</div>
                    </el-card>
                </el-col>
                <el-col :span="4">
                    <el-card shadow="hover" class="metric-card">
                        <div class="metric-title">在线人数</div>
                        <div class="metric-value">{{ latest.onlineCount || 0 }}</div>
                        <div class="metric-rate">&nbsp;</div>
                    </el-card>
                </el-col>
                <el-col :span="4">
                    <el-card shadow="hover" class="metric-card" :class="{ 'danger-card': isDanger('heapUsageRate') }">
                        <div class="metric-title">内存使用率</div>
                        <div class="metric-value">
                            <span :class="dangerClass('heapUsageRate')">{{ toFixed(latest.heapUsageRate) }}%</span>
                        </div>
                        <div class="metric-rate" v-if="latest.collectTime">
                            <span class="metric-sub">采集 {{ latest.collectTime }}</span>
                        </div>
                        <div class="metric-rate" v-else>&nbsp;</div>
                    </el-card>
                </el-col>
            </el-row>
        </div>

        <div style="padding:0 15px 15px 15px;">
            <el-row :gutter="16">
                <el-col :span="24">
                    <el-card shadow="hover">
                        <div slot="header" class="card-header">内存趋势图</div>
                        <div class="heap-summary">
                            <div class="heap-summary-col">
                                <div class="heap-summary-label">Size</div>
                                <div class="heap-summary-value">{{ formatBytes(heapSummary.size) }}</div>
                            </div>
                            <div class="heap-summary-col">
                                <div class="heap-summary-label">Max</div>
                                <div class="heap-summary-value">{{ formatBytes(heapSummary.max) }}</div>
                            </div>
                            <div class="heap-summary-col heap-summary-right">
                                <div class="heap-summary-label">Used</div>
                                <div class="heap-summary-value">{{ formatBytes(heapSummary.used) }}</div>
                            </div>
                        </div>
                        <div ref="heapChart" class="chart-box"></div>
                    </el-card>
                </el-col>
            </el-row>
        </div>

        <div style="padding:0 15px 15px 15px;">
            <el-row :gutter="16">
                <el-col :span="24">
                    <el-card shadow="hover">
                        <div slot="header" class="card-header">CPU趋势图 (%)</div>
                        <div ref="cpuChart" class="chart-box"></div>
                    </el-card>
                </el-col>
            </el-row>
        </div>

        <div style="padding:0 15px 30px 15px;">
            <el-row :gutter="16">
                <el-col :span="24">
                    <el-card shadow="hover">
                        <div slot="header" class="card-header">在线人数趋势</div>
                        <div ref="onlineChart" class="chart-box"></div>
                    </el-card>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
import * as echarts from "echarts";

const HEAP_RATE_THRESHOLD = 80;
const CPU_THRESHOLD = 80;
const GC_CPU_THRESHOLD = 30;

function pad(n) {
    return n < 10 ? "0" + n : "" + n;
}

function formatRange(days) {
    const end = new Date();
    const start = new Date(end.getTime() - days * 24 * 3600 * 1000);
    const fmt = d =>
        d.getFullYear() + "-" + pad(d.getMonth() + 1) + "-" + pad(d.getDate()) +
        " " + pad(d.getHours()) + ":" + pad(d.getMinutes()) + ":" + pad(d.getSeconds());
    return [fmt(start), fmt(end)];
}

export default {
    data() {
        const range = formatRange(1);
        return {
            servers: [],
            filters: {
                serverId: "",
                dateRange: range
            },
            latest: {},
            history: [],
            heapSummary: { size: 0, used: 0, max: 0 },
            heapChart: null,
            cpuChart: null,
            onlineChart: null
        };
    },
    methods: {
        formatMB(val) {
            if (val == null || val === "" || isNaN(val)) return "0.00";
            return Number(val).toFixed(2);
        },
        formatBytes(valMB) {
            if (valMB == null || valMB === "" || isNaN(valMB)) return "0 B";
            var n = Number(valMB) * 1024 * 1024;
            if (n < 1024) return Math.round(n) + " B";
            if (n < 1024 * 1024) return (n / 1024).toFixed(2) + " KB";
            if (n < 1024 * 1024 * 1024) return (n / (1024 * 1024)).toFixed(2) + " MB";
            return (n / (1024 * 1024 * 1024)).toFixed(2) + " GB";
        },
        refreshHeapSummary() {
            if (this.history && this.history.length) {
                var last = this.history[this.history.length - 1];
                this.heapSummary = {
                    size: Number(last.heapCommitted || 0),
                    used: Number(last.heapUsed || 0),
                    max: Number(last.heapMax || 0)
                };
            } else {
                this.heapSummary = {
                    size: Number(this.latest.heapCommitted || 0),
                    used: Number(this.latest.heapUsed || 0),
                    max: Number(this.latest.heapMax || 0)
                };
            }
        },
        toFixed(val) {
            if (val == null || val === "") return "0.00";
            return Number(val).toFixed(2);
        },
        isDanger(key) {
            if (key === "heapUsageRate") return Number(this.latest.heapUsageRate || 0) >= HEAP_RATE_THRESHOLD;
            if (key === "processCpuPercent") return Number(this.latest.processCpuPercent || 0) >= CPU_THRESHOLD;
            return false;
        },
        dangerClass(key) {
            return this.isDanger(key) ? "danger-text" : "";
        },
        handleSearch() {
            if (!this.filters.serverId) {
                this.$message({ message: "请选择服务器", type: "warning" });
                return;
            }
            if (!this.filters.dateRange || this.filters.dateRange.length !== 2) {
                this.$message({ message: "请选择时间范围", type: "warning" });
                return;
            }
            this.loadLatest();
            this.loadHistory();
        },
        handleReset() {
            const range = formatRange(1);
            this.filters.serverId = this.servers.length ? String(this.servers[0].id) : "";
            this.filters.dateRange = range;
            if (this.filters.serverId) {
                this.loadLatest();
                this.loadHistory();
            }
        },
        loadLatest() {
            this.$api.server.monitorLatest({ serverId: this.filters.serverId }).then(res => {
                if (res.code === 0 || res.code == null) {
                    this.latest = res.data ? res.data : {};
                } else {
                    this.latest = {};
                }
                this.refreshHeapSummary();
            });
        },
        loadHistory() {
            const params = {
                serverId: this.filters.serverId,
                startTime: this.filters.dateRange[0],
                endTime: this.filters.dateRange[1]
            };
            this.$api.server.monitorHistory(params).then(res => {
                let list = [];
                if (res.code === 0 || res.code == null) {
                    list = (res.data && res.data.list) || [];
                }
                this.history = list;
                this.refreshHeapSummary();
                this.$nextTick(() => {
                    this.renderHeapChart();
                    this.renderCpuChart();
                    this.renderOnlineChart();
                });
            });
        },
        loadServers() {
            this.$api.server.loadServerIds().then(res => {
                this.servers = res.ids || [];
                if (this.servers.length && !this.filters.serverId) {
                    this.filters.serverId = String(this.servers[0].id);
                    this.loadLatest();
                    this.loadHistory();
                }
            });
        },
        baseOption() {
            return {
                tooltip: {
                    trigger: "axis"
                },
                legend: {
                    data: []
                },
                grid: {
                    left: "3%",
                    right: "4%",
                    bottom: "3%",
                    containLabel: true
                },
                xAxis: {
                    type: "category",
                    boundaryGap: false,
                    data: []
                },
                yAxis: {
                    type: "value"
                },
                series: []
            };
        },
        renderHeapChart() {
            if (!this.heapChart) {
                this.heapChart = echarts.init(this.$refs.heapChart);
            }
            const times = this.history.map(i => i.collectTime);
            const used = this.history.map(i => Number(i.heapUsed || 0));
            const committed = this.history.map(i => Number(i.heapCommitted || 0));
            const option = {
                tooltip: {
                    trigger: "axis",
                    formatter: function (params) {
                        if (!params || !params.length) return "";
                        let html = params[0].axisValueLabel + "<br/>";
                        for (let i = 0; i < params.length; i++) {
                            html += params[i].marker + " " + params[i].seriesName + ": " +
                                Number(params[i].data).toFixed(2) + " MB<br/>";
                        }
                        return html;
                    }
                },
                legend: {
                    right: 10,
                    bottom: 0,
                    orient: "horizontal",
                    icon: "roundRect",
                    itemWidth: 14,
                    itemHeight: 14,
                    data: ["Heap size", "Used heap"]
                },
                grid: {
                    left: "3%",
                    right: "4%",
                    top: 10,
                    bottom: 40,
                    containLabel: true
                },
                xAxis: {
                    type: "category",
                    boundaryGap: false,
                    axisLine: { lineStyle: { color: "#B0B6BB" } },
                    axisLabel: { color: "#606266" },
                    splitLine: { show: true, lineStyle: { color: "#EBEEF5", type: "dashed" } },
                    data: times
                },
                yAxis: {
                    type: "value",
                    axisLine: { show: false },
                    axisTick: { show: false },
                    axisLabel: {
                        color: "#606266",
                        formatter: "{value} MB"
                    },
                    splitLine: { lineStyle: { color: "#EBEEF5", type: "dashed" } }
                },
                series: [
                    {
                        name: "Heap size",
                        type: "line",
                        showSymbol: false,
                        smooth: false,
                        stack: null,
                        lineStyle: { width: 2, color: "#F5A623" },
                        itemStyle: { color: "#F5A623" },
                        areaStyle: {
                            color: {
                                type: "linear", x: 0, y: 0, x2: 0, y2: 1,
                                colorStops: [
                                    { offset: 0, color: "rgba(245, 166, 35, 0.45)" },
                                    { offset: 1, color: "rgba(245, 166, 35, 0.12)" }
                                ]
                            }
                        },
                        data: committed
                    },
                    {
                        name: "Used heap",
                        type: "line",
                        showSymbol: false,
                        smooth: false,
                        lineStyle: { width: 2, color: "#2F9DF0" },
                        itemStyle: { color: "#2F9DF0" },
                        areaStyle: {
                            color: {
                                type: "linear", x: 0, y: 0, x2: 0, y2: 1,
                                colorStops: [
                                    { offset: 0, color: "rgba(47, 157, 240, 0.5)" },
                                    { offset: 1, color: "rgba(47, 157, 240, 0.10)" }
                                ]
                            }
                        },
                        data: used
                    }
                ]
            };
            this.heapChart.setOption(option, true);
        },
        renderCpuChart() {
            if (!this.cpuChart) {
                this.cpuChart = echarts.init(this.$refs.cpuChart);
            }
            const option = this.baseOption();
            option.legend.data = ["processCpuPercent"];
            option.legend = {
                show: false
            };
            const times = this.history.map(i => i.collectTime);
            const proc = this.history.map(i => Number(i.processCpuPercent || 0));
            option.xAxis.data = times;
            option.series = [
                { name: "processCpuPercent", type: "line", smooth: true, data: proc }
            ];
            this.cpuChart.setOption(option, true);
        },
        renderOnlineChart() {
            if (!this.onlineChart) {
                this.onlineChart = echarts.init(this.$refs.onlineChart);
            }
            const option = this.baseOption();
            option.legend.data = ["onlineCount"];
            option.legend = {
                show: false
            };
            const times = this.history.map(i => i.collectTime);
            const online = this.history.map(i => Number(i.onlineCount || 0));
            option.xAxis.data = times;
            option.series = [
                { name: "onlineCount", type: "line", smooth: true, areaStyle: {}, data: online }
            ];
            this.onlineChart.setOption(option, true);
        },
        handleResize() {
            this.heapChart && this.heapChart.resize();
            this.cpuChart && this.cpuChart.resize();
            this.onlineChart && this.onlineChart.resize();
        }
    },
    mounted() {
        window.addEventListener("resize", this.handleResize);
        this.loadServers();
    },
    beforeDestroy() {
        window.removeEventListener("resize", this.handleResize);
        this.heapChart && this.heapChart.dispose();
        this.cpuChart && this.cpuChart.dispose();
        this.onlineChart && this.onlineChart.dispose();
    }
};
</script>

<style scoped>
.metric-card {
    text-align: left;
}

.metric-title {
    font-size: 13px;
    color: #909399;
    margin-bottom: 8px;
}

.metric-value {
    font-size: 22px;
    font-weight: 600;
    color: #303133;
}

.metric-value .metric-sub {
    font-size: 14px;
    font-weight: 400;
    color: #909399;
}

.metric-rate {
    font-size: 12px;
    margin-top: 6px;
    color: #606266;
}

.metric-rate .metric-sub {
    color: #909399;
}

.danger-text {
    color: #f56c6c !important;
}

.danger-card {
    border: 1px solid #f56c6c;
}

.card-header {
    font-weight: 600;
}

.heap-summary {
    display: flex;
    align-items: flex-start;
    padding: 8px 4px 14px 4px;
}

.heap-summary-col {
    flex: 1;
}

.heap-summary-right {
    text-align: right;
}

.heap-summary-label {
    color: #606266;
    font-size: 14px;
    margin-bottom: 2px;
}

.heap-summary-value {
    color: #303133;
    font-weight: 600;
    font-size: 18px;
    letter-spacing: 0.2px;
}

.chart-box {
    width: 100%;
    height: 320px;
}
</style>
