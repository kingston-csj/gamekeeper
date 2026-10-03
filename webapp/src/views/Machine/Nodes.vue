<template>
    <div class="page-container">
        <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
            <el-form :inline="true" :model="filters" :size="size">
                <el-form-item>
                    <el-input v-model="filters.label" placeholder="名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="fa fa-search" @click="findPage()">{{ $t('action.search')
                    }}</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="fa fa-plus" @click="handleAdd">{{ $t('action.add') }}</el-button>
                </el-form-item>
            </el-form>
        </div>

        <el-table :data="tableData" stripe size="mini" style="width: 100%;" row-key="id" v-loading="loading"
            element-loading-text="$t('action.loading')">
            <el-table-column prop="id" header-align="center" align="center" width="200" label="机器id" />
            <el-table-column prop="name" header-align="center" align="center" width="200" label="机器名称" />
            <el-table-column prop="ip" header-align="center" align="center" label="机器ip" />
            <el-table-column prop="agentUrl" header-align="center" align="center" label="agentUrl地址" />
            <el-table-column fixed="right" header-align="center" align="center" width="200"
                :label="$t('action.operation')">
                <template slot-scope="scope">
                    <el-button size="mini" icon="fa fa-edit" @click="handleEdit(scope.row)">{{ $t('action.edit')
                    }}</el-button>
                    <el-button size="mini" icon="fa fa-trash" type="danger" @click="handleDelete(scope.row)">{{
                        $t('action.delete') }}</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog :title="!dataForm.id ? '新增' : '修改'" width="40%" :visible.sync="dialogVisible"
            :close-on-click-modal="false">
            <el-form :model="dataForm" ref="dataForm" label-width="120px" :size="size" style="text-align:left;">
                <el-form-item label="id" prop="id">
                    <el-input v-model="dataForm.id" placeholder="机器id"></el-input>
                </el-form-item>
                <el-form-item label="名称" prop="name">
                    <el-input v-model="dataForm.name" placeholder="机器名称"></el-input>
                </el-form-item>
                <el-form-item label="机器ip" prop="ip">
                    <el-input v-model="dataForm.ip" placeholder="机器ip"></el-input>
                </el-form-item>
                <el-form-item label="agentUrl" prop="agentUrl">
                    <el-input v-model="dataForm.agentUrl" placeholder="agentUrl地址"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button :size="size" @click="dialogVisible = false">{{ $t('action.cancel') }}</el-button>
                <el-button :size="size" type="primary" @click="submitForm()">{{ $t('action.comfirm') }}</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import KtButton from "@/views/Core/KtButton"

export default {
    components: {
        KtButton
    },
    data() {
        return {
            size: 'small',
            filters: {
                label: ''
            },
            dialogVisible: false,
            dataForm: {
                id: 0,
                name: '',
                ip: ''
            },
            loading: false,
            editLoading: false,
            tableData: []
        }
    },
    methods: {
        findPage() {
            this.loading = true
            this.$api.machine.findPage().then((res) => {
                this.tableData = res.data.servers || []
            }).finally(() => {
                this.loading = false
            })
        },
        handleAdd() {
            this.dialogVisible = true
            this.dataForm = {
                id: 0,
                name: '',
                ip: ''
            }
        },
        handleEdit(row) {
            if (row.id <= 0) {
                return
            }
            this.dialogVisible = true
            this.dataForm = Object.assign({}, row)
        },
        handleDelete(row) {
            this.$confirm('确认删除选中记录吗？', '提示', {
                type: 'warning'
            }).then(() => {
                this.$api.machine.deleteNode({ id: row.id }).then(() => {
                    this.findPage()
                    this.$message({ message: '删除成功', type: 'success' })
                })
            })
        },
        submitForm() {
            this.$refs['dataForm'].validate((valid) => {
                if (this.dataForm.id <= 0) {
                    this.$message({ message: '操作失败', type: 'error' })
                    return
                }
                if (valid) {
                    this.$confirm('确认提交吗？', '提示', {}).then(() => {
                        this.editLoading = true
                        const params = Object.assign({}, this.dataForm)
                        this.$api.machine.saveNode(params).then((res) => {
                            this.editLoading = false
                            if (res.code == 0) {
                                this.$message({ message: '操作成功', type: 'success' })
                                this.dialogVisible = false
                                this.$refs['dataForm'].resetFields()
                            } else {
                                this.$message({ message: '操作失败, ' + res.msg, type: 'error' })
                            }
                            this.findPage()
                        }).catch(() => {
                            this.editLoading = false
                        })
                    })
                }
            })
        }
    },
    mounted() {
        this.findPage()
    }
}
</script>
