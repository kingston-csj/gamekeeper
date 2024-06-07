<template>
  <div class="page-container">
  <!--工具栏-->
  <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
    <el-form :inline="true" :model="filters" :size="size">
      <el-form-item>
        <el-input v-model="filters.label" placeholder="名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button  type="primary" icon="fa fa-search"  @click="findPage()">{{$t('action.search')}}</el-button>
      </el-form-item>
      <el-form-item>
				<kt-button icon="fa fa-plus" :label="$t('action.add')" perms="server:nodes:add" type="primary" @click="handleAdd"/>
			</el-form-item>
    </el-form>
  </div>

  	<!--表格树内容栏-->
    <el-table :data="tableData" stripe size="mini" style="width: 100%;"
      rowKey="id" v-loading="loading" element-loading-text="$t('action.loading')">
      <el-table-column
        prop="id" header-align="center" align="center" width="80" label="服务器id">
      </el-table-column>
      <el-table-column 
        prop="name" header-align="center" align="center" width="120" label="服务器名称">
      </el-table-column>
      <el-table-column
        prop="ip" header-align="center" align="center" label="服务器ip">
      </el-table-column>
      <el-table-column
        prop="onlinePlayerSum" header-align="center" align="center" label="在线人数">
      </el-table-column>
      <el-table-column
        prop="httpPort" header-align="center" align="center" label="http端口">
      </el-table-column>
      <el-table-column
        fixed="right" header-align="center" align="center" width="185" :label="$t('action.operation')">
        <template slot-scope="scope">
          <kt-button icon="fa fa-edit" :label="$t('action.edit')" perms="server:nodes:edit" @click="handleEdit(scope.row)"/>
          <kt-button icon="fa fa-trash" :label="$t('action.delete')" perms="server:nodes:delete" type="danger" @click="handleDelete(scope.row)"/>
        </template>
      </el-table-column>
    </el-table>
    <!-- 新增/修改界面 -->
    <el-dialog :title="!dataForm.id ? '新增' : '修改'" width="40%" :visible.sync="dialogVisible" :close-on-click-modal="false">
      <el-form :model="dataForm"  ref="dataForm" 
        label-width="80px" :size="size" style="text-align:left;">
        <el-form-item label="id" prop="id">
          <el-input v-model="dataForm.id" placeholder="服务器id"></el-input>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="dataForm.name" placeholder="服务器名称"></el-input>
        </el-form-item>
        <el-form-item label="服务器ip" prop="ip">
          <el-input v-model="dataForm.ip" placeholder="服务器ip"></el-input>
        </el-form-item>
        <el-form-item label="http端口" prop="httpPort">
          <el-input v-model="dataForm.httpPort" placeholder="http端口"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button :size="size"  @click="dialogVisible = false">{{$t('action.cancel')}}</el-button>
        <el-button :size="size"  type="primary" @click="submitForm()">{{$t('action.comfirm')}}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"

export default {
  components:{
      KtTable,
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
        ip: '',
        httpPort: 0
      },
      loading: false,
      tableData:[],

    }
  },
  methods: {
    // 获取分页数据
    findPage: function () {
      this.$api.server.findPage(this.pageRequest).then((res) => {
        this.tableData = res.data.servers
        console.info("-0------------"+JSON.stringify(this.tableData))
      })
    },

    // 显示新增界面
		handleAdd: function () {
			this.dialogVisible = true
			this.dataForm = {
        id: 0,
        name: '',
        ip: '',
        httpPort: 0
      }
		},
		// 显示编辑界面
		handleEdit: function (row) {
      if (row.id <= 0) {
        return
      }
      this.dialogVisible = true
      Object.assign(this.dataForm, row);
      console.info("-handleEdit------------"+JSON.stringify(this.dataForm))
		},
    // 删除
    handleDelete (row) {
      this.$confirm('确认删除选中记录吗？', '提示', {
				type: 'warning'
      }).then(() => {
        let params = {
          id:row.id
        }
        this.$api.server.deleteNode(params).then( res => {
          this.findPage()
          this.$message({message: '删除成功', type: 'success'})
        })
      })
    },
    // 表单提交
    submitForm () {
      this.$refs['dataForm'].validate((valid) => {
        if (this.dataForm.id <= 0) {
          this.$message({ message: '操作失败', type: 'error' })
          return
        }
        if (valid) {
					this.$confirm('确认提交吗？', '提示', {}).then(() => {
						this.editLoading = true
						let params = Object.assign({}, this.dataForm)
						this.$api.server.saveNode(params).then((res) => {
              this.editLoading = false
              if(res.code == 200) {
								this.$message({ message: '操作成功', type: 'success' })
                this.dialogVisible = false
                this.$refs['dataForm'].resetFields()
							} else {
								this.$message({message: '操作失败, ' + res.msg, type: 'error'})
							}
							this.findPage()
						})
					})
				}
      })
    },
  
  },

  mounted() {
  }
}
</script>

