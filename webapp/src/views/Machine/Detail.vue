<template>
    <div class="page-container trend-page">
        <div class="filter-bar">
            <div class="filter-left">
                <span class="filter-label">目标机器</span>
                <el-select v-model="filters.hostIp" placeholder="请选择机器" size="small" style="width: 280px;"
                    @change="handleFilterChange">
                    <el-option v-for="item in machines" :key="item.ip" :label="machineLabel(item)" :value="item.ip" />
                </el-select>
            </div>
            <div class="filter-right">
                <div class="quick-buttons">
                    <el-button v-for="item in quickOptions" :key="item.key" size="small"
                        :type="activeQuickKey === item.key ? 'primary' : 'default'" @click="handleQuickRange(item)">
                        {{ item.label }}
                    </el-button>
                </div>
                <el-date-picker v-model="filters.dateRange" size="small" type="datetimerange" range-separator="至"
                    start-placeholder="开始时间" end-placeholder="结束时间" value-format="yyyy-MM-dd HH:mm:ss"
                    format="yyyy-MM-dd HH:mm:ss" style="width: 380px;" @change="handleDateRangeChange" />
            </div>
        </div>

        <el-row :gutter="16" class="chart-grid">
            <el-col :span="12">
                <el-card shadow="hover" class="chart-card">
                    <div slot="header" class="card-header">CPU使用率趋势图</div>
                    <div ref="cpuChart" class="chart-box"></div>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card shadow="hover" class="chart-card">
                    <div slot="header" class="card-header">内存使用率趋势图</div>
                    <div ref="memChart" class="chart-box"></div>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card shadow="hover" class="chart-card">
                    <div slot="header" class="card-header">磁盘使用率趋势图</div>
                    <div ref="diskChart" class="chart-box"></div>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card shadow="hover" class="chart-card">
                    <div slot="header" class="card-header">系统负载趋势图</div>
                    <div ref="loadChart" class="chart-box"></div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import * as echarts from "echarts"

function pad(n) {
    return n < 10 ? "0" + n : "" + n
}

function formatDateTime(date) {
    return date.getFullYear() + "-" + pad(date.getMonth() + 1) + "-" + pad(date.getDate())
        + " " + pad(date.getHours()) + ":" + pad(date.getMinutes()) + ":" + pad(date.getSeconds())
}

function createRange(hours) {
    const end = new Date()
    const start = new Date(end.getTime() - hours * 60 * 60 * 1000)
    return [formatDateTime(start), formatDateTime(end)]
}

export default {
    data() {
        return {
            machines: [],
            history: [],
            loading: false,
            activeQuickKey: "24h",
            filters: {
                hostIp: "",
                dateRange: createRange(24)
            },
            quickOptions: [
                { key: "1h", label: "最近1小时", hours: 1 },
                { key: "24h", label: "最近24小时", hours: 24 },
                { key: "7d", label: "最近7天", hours: 24 * 7 },
                { key: "30d", label: "最近30天", hours: 24 * 30 }
            ],
            cpuChart: null,
            memChart: null,
            diskChart: null,
            loadChart: null
        }
    },
    methods: {
        machineLabel(item) {
            return (item.name || "-") + " (" + (item.ip || "-") + ")"
        },
        loadMachines() {
            this.$api.machine.loadServerIds().then((res) => {
                this.machines = res.ids || []
                if (!this.filters.hostIp && this.machines.length) {
                    this.filters.hostIp = this.machines[0].ip
                }
                if (this.filters.hostIp) {
                    this.loadHistory()
                }
            })
        },
        handleQuickRange(item) {
            this.activeQuickKey = item.key
            this.filters.dateRange = createRange(item.hours)
            this.loadHistory()
        },
        handleDateRangeChange() {
            this.activeQuickKey = ""
            this.loadHistory()
        },
        handleFilterChange() {
            this.loadHistory()
        },
        loadHistory() {
            if (!this.filters.hostIp || !this.filters.dateRange || this.filters.dateRange.length !== 2) {
                return
            }
            this.loading = true
            this.$api.machine.monitorHistory({
                hostIp: this.filters.hostIp,
                startTime: this.filters.dateRange[0],
                endTime: this.filters.dateRange[1]
            }).then((res) => {
                this.history = (res.data && res.data.list) || []
                this.$nextTick(() => {
                    this.renderCharts()
                })
            }).finally(() => {
                this.loading = false
            })
        },
        renderCharts() {
            this.renderMetricChart("cpuChart", "CPU使用率", "cpuUsage", 100, [70, 90], ["#e6a23c", "#f56c6c"], "%")
            this.renderMetricChart("memChart", "内存使用率", "memUsage", 100, [70, 90], ["#e6a23c", "#f56c6c"], "%")
            this.renderMetricChart("diskChart", "磁盘使用率", "diskUsage", 100, [70, 90], ["#e6a23c", "#f56c6c"], "%")
            this.renderLoadChart()
        },
        renderLoadChart() {
            if (!this.loadChart) {
                this.loadChart = echarts.init(this.$refs.loadChart)
            }
            const chart = this.loadChart
            const xData = this.history.map(item => this.formatAxisTime(item.createTime))
            const load1Data = this.history.map(item => Number(item.load1 || 0))
            const load5Data = this.history.map(item => Number(item.load5 || 0))
            const load15Data = this.history.map(item => Number(item.load15 || 0))
            const thresholdValues = [1, 2]
            const thresholdColors = ["#e6a23c", "#f56c6c"]
            const markLineData = thresholdValues.map((value, index) => ({
                yAxis: value,
                lineStyle: {
                    color: thresholdColors[index],
                    type: "dashed"
                },
                label: {
                    formatter: value
                }
            }))
            chart.setOption({
                tooltip: {
                    trigger: "axis"
                },
                legend: {
                    data: ["load1", "load5", "load15"],
                    right: "5%",
                    top: "2%"
                },
                grid: {
                    left: "5%",
                    right: "5%",
                    top: "18%",
                    bottom: "12%",
                    containLabel: true
                },
                xAxis: {
                    type: "category",
                    boundaryGap: false,
                    data: xData
                },
                yAxis: {
                    type: "value",
                    min: 0
                },
                series: [
                    {
                        name: "load1",
                        type: "line",
                        smooth: true,
                        showSymbol: false,
                        data: load1Data,
                        markLine: {
                            symbol: "none",
                            data: markLineData
                        }
                    },
                    {
                        name: "load5",
                        type: "line",
                        smooth: true,
                        showSymbol: false,
                        data: load5Data
                    },
                    {
                        name: "load15",
                        type: "line",
                        smooth: true,
                        showSymbol: false,
                        data: load15Data
                    }
                ]
            }, true)
        },
        renderMetricChart(refName, title, field, maxValue, thresholdValues, thresholdColors, unit) {
            if (!this[refName]) {
                this[refName] = echarts.init(this.$refs[refName])
            }
            const chart = this[refName]
            const xData = this.history.map(item => this.formatAxisTime(item.createTime))
            const seriesData = this.history.map(item => Number(item[field] || 0))
            const markLineData = thresholdValues.map((value, index) => ({
                yAxis: value,
                lineStyle: {
                    color: thresholdColors[index],
                    type: "dashed"
                },
                label: {
                    formatter: value + unit
                }
            }))
            chart.setOption({
                tooltip: {
                    trigger: "axis",
                    formatter: (params) => {
                        if (!params || !params.length) {
                            return ""
                        }
                        const item = params[0]
                        const point = this.history[item.dataIndex]
                        return (point ? point.createTime : item.axisValueLabel) + "<br/>" + item.marker + " " + title + ": " + Number(item.data).toFixed(2) + unit
                    }
                },
                grid: {
                    left: "5%",
                    right: "5%",
                    top: "10%",
                    bottom: "12%",
                    containLabel: true
                },
                xAxis: {
                    type: "category",
                    boundaryGap: false,
                    data: xData
                },
                yAxis: {
                    type: "value",
                    min: 0,
                    max: maxValue == null ? null : maxValue
                },
                series: [
                    {
                        name: title,
                        type: "line",
                        smooth: true,
                        showSymbol: false,
                        data: seriesData,
                        markLine: {
                            symbol: "none",
                            data: markLineData
                        }
                    }
                ]
            }, true)
        },
        formatAxisTime(value) {
            if (!value) {
                return ""
            }
            return value.substring(0, 16)
        },
        handleResize() {
            this.cpuChart && this.cpuChart.resize()
            this.memChart && this.memChart.resize()
            this.diskChart && this.diskChart.resize()
            this.loadChart && this.loadChart.resize()
        }
    },
    mounted() {
        window.addEventListener("resize", this.handleResize)
        this.loadMachines()
    },
    beforeDestroy() {
        window.removeEventListener("resize", this.handleResize)
        this.cpuChart && this.cpuChart.dispose()
        this.memChart && this.memChart.dispose()
        this.diskChart && this.diskChart.dispose()
        this.loadChart && this.loadChart.dispose()
    }
}
</script>

<style scoped>
.trend-page {
    padding: 15px;
    background: #f5f7fa;
}

.filter-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    gap: 12px;
    flex-wrap: wrap;
}

.filter-left,
.filter-right {
    display: flex;
    align-items: center;
    gap: 12px;
    flex-wrap: wrap;
}

.filter-label {
    color: #606266;
    font-size: 13px;
}

.quick-buttons {
    display: flex;
    gap: 8px;
}

.chart-grid .el-col {
    margin-bottom: 16px;
}

.chart-card {
    height: 100%;
}

.card-header {
    font-weight: 600;
    color: #303133;
}

.chart-box {
    width: 100%;
    height: 320px;
}
</style>
