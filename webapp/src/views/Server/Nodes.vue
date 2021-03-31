<template>
  <div class="page-container">
  <!--工具栏-->
  <div class="toolbar" style="float:left;padding-top:10px;padding-left:15px;">
    <el-form :inline="true" :model="filters" :size="size">
      <el-form-item>
        <el-input v-model="filters.label" placeholder="名称"></el-input>
      </el-form-item>
      <el-form-item>
        <kt-button icon="fa fa-search" :label="$t('action.search')" perms="sys:dict:view" type="primary" @click="findPage(null)"/>
      </el-form-item>
    </el-form>
  </div>


  <!--表格内容栏-->
  <kt-table :height="350" permsEdit="sys:dict:edit" permsDelete="sys:dict:delete"
    :data="pageResult" :columns="columns"  :showOperation="false" showBatchDelete=“false”
    @findPage="findPage" @handleEdit="handleEdit" @handleDelete="handleDelete">
  </kt-table>

  </div>
</template>

<script>
import KtTable from "@/views/Core/KtTable"
import KtButton from "@/views/Core/KtButton"
import { format } from "@/utils/datetime"
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
      columns: [
        {prop:"name", label:"服务器名称", minWidth:100},
        {prop:"ip", label:"服务器ip", minWidth:100},
        {prop:"onlinePlayerSum", label:"在线人数", minWidth:80},
        {prop:"cachePlayerSum", label:"缓存人数", minWidth:80}

      ],
      pageRequest: { pageNum: 1, pageSize: 10 },
      pageResult: {},

      dataFormRules: {
        label: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ]
      },

    }
  },
  methods: {
    // 获取分页数据
    findPage: function (data) {
      if(data !== null) {
        this.pageRequest = data.pageRequest
      }
      this.$api.server.findPage(this.pageRequest).then((res) => {
        this.pageResult = res.data
      }).then(data!=null?data.callback:'')
    },

  
    // 时间格式化
        dateFormat: function (row, column, cellValue, index){
            return format(row[column.property])
        }
  },
  mounted() {
  }
}
</script>

<style scoped>

</style>