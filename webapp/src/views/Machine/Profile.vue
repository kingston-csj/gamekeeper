<template>
    <div class="page-container profile-page">
        <div class="toolbar">
            <div class="page-title">机器总览</div>
            <el-button size="small" type="primary" icon="fa fa-refresh" @click="loadOverview">刷新</el-button>
        </div>

        <div class="selected-node" v-if="selectedNode">
            当前节点：{{ selectedNode.hostName || '-' }} / {{ selectedNode.hostIp || '-' }}
        </div>

        <div class="summary-strip">
            <div class="summary-item">机器总数 {{ overview.totalCount || 0 }}</div>
            <div class="summary-item high">高风险 {{ overview.highCount || 0 }}</div>
            <div class="summary-item medium">中风险 {{ overview.mediumCount || 0 }}</div>
            <div class="summary-item low">低风险 {{ overview.lowCount || 0 }}</div>
        </div>

        <div class="layout-block top-block">
            <el-card shadow="hover" class="panel-card">
                <div slot="header" class="panel-header">机器风险排名</div>
                <el-table :data="riskList" stripe size="mini" v-loading="loading" highlight-current-row
                    @row-click="handleRowClick">
                    <el-table-column label="风险等级" width="110" header-align="center" align="center">
                        <template slot-scope="scope">
                            <el-tag size="mini" :type="riskTagType(scope.row.riskLevel)">
                                {{ riskLabel(scope.row.riskLevel) }}
                            </el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column prop="hostIp" label="主机IP" min-width="150" />
                    <el-table-column prop="hostName" label="主机名" min-width="140" />
                    <el-table-column label="CPU使用率(%)" min-width="120" header-align="center" align="center">
                        <template slot-scope="scope">
                            <span :class="metricClass('cpuUsage', scope.row.cpuUsage)">{{
                                formatPercent(scope.row.cpuUsage) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="内存使用率(%)" min-width="120" header-align="center" align="center">
                        <template slot-scope="scope">
                            <span :class="metricClass('memUsage', scope.row.memUsage)">{{
                                formatPercent(scope.row.memUsage) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="磁盘使用率(%)" min-width="120" header-align="center" align="center">
                        <template slot-scope="scope">
                            <span :class="metricClass('diskUsage', scope.row.diskUsage)">{{
                                formatPercent(scope.row.diskUsage) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="1分钟系统负载" min-width="120" header-align="center" align="center">
                        <template slot-scope="scope">
                            <span :class="metricClass('load1', scope.row.load1)">{{ formatNumber(scope.row.load1)
                                }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="updateTime" label="最后更新时间" min-width="170" />
                </el-table>
            </el-card>
        </div>

        <div class="layout-block bottom-block">
            <el-row :gutter="16">
                <el-col :span="6" v-for="card in displayCards" :key="card.metricKey">
                    <el-card shadow="hover" class="metric-card" :class="'level-' + (card.level || 'low')">
                        <div class="metric-title">{{ card.metricName }}</div>
                        <div class="metric-value" :class="metricClass(card.metricKey, card.value)">
                            <template v-if="card.metricKey === 'load1'">{{ formatNumber(card.value) }}</template>
                            <template v-else>{{ formatPercent(card.value) }}</template>
                        </div>
                        <div class="metric-footer">
                            <div>{{ card.hostIp || '-' }}</div>
                            <div>{{ card.updateTime || '-' }}</div>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            loading: false,
            overview: {},
            riskList: [],
            summaryCards: [],
            selectedNode: null
        }
    },
    computed: {
        displayCards() {
            if (!this.selectedNode) {
                return this.summaryCards
            }
            return [
                this.buildSelectedCard('cpuUsage', 'CPU使用率', this.selectedNode.cpuUsage),
                this.buildSelectedCard('memUsage', '内存使用率', this.selectedNode.memUsage),
                this.buildSelectedCard('diskUsage', '磁盘使用率', this.selectedNode.diskUsage),
                this.buildSelectedCard('load1', '系统平均负载', this.selectedNode.load1)
            ]
        }
    },
    methods: {
        loadOverview() {
            this.loading = true
            this.$api.machine.monitorOverview().then((res) => {
                const data = res.data || {}
                this.overview = data
                this.riskList = data.riskList || []
                this.summaryCards = data.summaryCards || []
                this.selectedNode = this.riskList.length ? this.riskList[0] : null
            }).finally(() => {
                this.loading = false
            })
        },
        handleRowClick(row) {
            this.selectedNode = row
        },
        buildSelectedCard(metricKey, metricName, value) {
            return {
                metricKey: metricKey,
                metricName: metricName,
                value: value,
                hostIp: this.selectedNode ? this.selectedNode.hostIp : '-',
                hostName: this.selectedNode ? this.selectedNode.hostName : '-',
                updateTime: this.selectedNode ? this.selectedNode.updateTime : '-',
                level: this.metricLevel(metricKey, value)
            }
        },
        riskLabel(level) {
            if (level === 'high') {
                return '高'
            }
            if (level === 'medium') {
                return '中'
            }
            return '低'
        },
        riskTagType(level) {
            if (level === 'high') {
                return 'danger'
            }
            if (level === 'medium') {
                return 'warning'
            }
            return 'success'
        },
        metricLevel(metricKey, value) {
            const number = Number(value || 0)
            if (metricKey === 'cpuUsage') {
                return this.rangeLevel(number, 70, 85)
            }
            if (metricKey === 'memUsage') {
                return this.rangeLevel(number, 75, 90)
            }
            if (metricKey === 'diskUsage') {
                return this.rangeLevel(number, 80, 90)
            }
            return this.rangeLevel(number, 2, 5)
        },
        metricClass(metricKey, value) {
            const number = Number(value || 0)
            if (metricKey === 'cpuUsage') {
                return this.rangeClass(number, 70, 85)
            }
            if (metricKey === 'memUsage') {
                return this.rangeClass(number, 75, 90)
            }
            if (metricKey === 'diskUsage') {
                return this.rangeClass(number, 80, 90)
            }
            return this.rangeClass(number, 2, 5)
        },
        rangeClass(value, warn, danger) {
            if (value >= danger) {
                return 'text-danger'
            }
            if (value >= warn) {
                return 'text-warning'
            }
            return 'text-success'
        },
        rangeLevel(value, warn, danger) {
            if (value >= danger) {
                return 'high'
            }
            if (value >= warn) {
                return 'medium'
            }
            return 'low'
        },
        formatPercent(value) {
            return this.formatNumber(value) + '%'
        },
        formatNumber(value) {
            if (value == null || value === '') {
                return '0.00'
            }
            return Number(value).toFixed(2)
        }
    },
    mounted() {
        this.loadOverview()
    }
}
</script>

<style scoped>
.profile-page {
    padding: 15px;
    min-height: calc(100vh - 84px);
    background: #f5f7fa;
}

.toolbar {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;
}

.page-title {
    font-size: 20px;
    font-weight: 600;
    color: #303133;
}

.selected-node {
    margin-bottom: 12px;
    color: #606266;
    font-size: 13px;
}

.summary-strip {
    display: flex;
    gap: 12px;
    margin-bottom: 16px;
}

.summary-item {
    padding: 8px 14px;
    border-radius: 4px;
    background: #ffffff;
    color: #606266;
    font-size: 13px;
}

.summary-item.high {
    color: #f56c6c;
}

.summary-item.medium {
    color: #e6a23c;
}

.summary-item.low {
    color: #67c23a;
}

.layout-block {
    margin-bottom: 16px;
}

.top-block {
    height: calc((100vh - 170px) * 0.65);
    min-height: 360px;
}

.bottom-block {
    height: calc((100vh - 170px) * 0.35);
    min-height: 180px;
}

.panel-card {
    height: 100%;
}

.panel-header {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
}

.metric-card {
    border-left: 4px solid #67c23a;
}

.metric-card.level-medium {
    border-left-color: #e6a23c;
}

.metric-card.level-high {
    border-left-color: #f56c6c;
}

.metric-title {
    font-size: 14px;
    color: #909399;
    margin-bottom: 16px;
}

.metric-value {
    font-size: 30px;
    font-weight: 600;
    line-height: 1.2;
    margin-bottom: 18px;
}

.metric-footer {
    font-size: 12px;
    color: #909399;
    line-height: 1.6;
}

.text-success {
    color: #67c23a;
}

.text-warning {
    color: #e6a23c;
}

.text-danger {
    color: #f56c6c;
}
</style>
