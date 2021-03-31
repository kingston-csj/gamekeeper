<template>
  <div id="users">
    <el-form :inline="true" class="demo-form-inline">
         
      <el-form-item>
        <el-date-picker v-model="selectDateFrom" type="datetime" placeholder="开始日期" :picker-options="pickerOptions0">
        </el-date-picker>
      </el-form-item>
            
      <el-form-item>
        <el-date-picker v-model="selectDateTo" type="datetime" placeholder="结束日期" :picker-options="pickerOptions0">
        </el-date-picker>
      </el-form-item>
            
      <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
      </el-form-item>
        
    </el-form>
        
    <el-table :data="tableData" style="width: 100%" v-loading="loading2" element-loading-text="拼命加载中">
      <el-table-column type="selection" width="55">

      </el-table-column>
      <el-table-column prop="id" label="订单id" width="180">
         <template slot-scope="scope">{{ scope.row.id}}</template>
      </el-table-column> 
      <el-table-column prop="account" label="充值账号">
         <template slot-scope="scope">{{ scope.row.account}}</template>
      </el-table-column> 
      <el-table-column prop="time" label="充值时间" width="180">
        <template slot-scope="scope">{{ scope.row.time}}</template>
      </el-table-column>
      <el-table-column prop="money" label="充值金额" width="180">
        <template slot-scope="scope">{{ scope.row.money}}</template>
      </el-table-column>
      <el-table-column prop="channel" label="归属渠道" width="180">
        <template slot-scope="scope">{{ scope.row.channel}}</template>
      </el-table-column>

    </el-table> 


    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[10, 20, 50]" 
        :page-size="pageSize"         
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">   
    </el-pagination>
     
  </div>
</template>
<style type="text/css" scoped>
  .block {
    margin-top: 20px;
    width: 100%;
  }
  .block .r_btn {
    float: left;
    width: 90px;
  }
  .block .r_page {
     float: right;
  }
</style>
<script type="text/javascript">
 
  export default {
    data() {
      return {
        tableData: [],
        currentPage:1,
        total:0,
        pageSize:10,
        selectDateFrom:'',
        selectDateTo:'',
        dialogFormVisible: false,
        formLabelWidth: '120px',
        loading2: false,

        pickerOptions0: {
            disabledDate(time) {
              return false;
            }
        },

      }
    },
    mounted() {
      this.initTime();
      //this.loadData();
    },
    methods: {
      //加载数据
      loadData() {
        this.loading2 = true;
        var params = {
          page: this.currentPage, 
          pageSize: this.pageSize, 
          selectFrom: this.selectDateFrom, 
          selectTo: this.selectDateTo
        };
       
        var _this = this;

        this.$api.pay.findPage({
          page: _this.currentPage,
          pageSize: _this.pageSize,
          selectFrom:_this.selectDateFrom.getTime(),
          selectTo:_this.selectDateTo.getTime()
        }).then((res) => {
          _this.loading = false;
          _this.tableData = res.data.orders;
          _this.loading2 = false;
          _this.total = res.data.totalRecord;
        })


      },
      //查询
      onSubmit() {
        this.loadData();
      },
      //改变分页大小
      handleSizeChange(val) {
        this.pageSize = val;
      },
      //跳转页数
      handleCurrentChange(val) {
        this.currentPage = val;
        this.loadData();
      },

      initTime() {
        var from = new Date()
        from.setHours(0)
        from.setMinutes(0)
        from.setSeconds(0)
        this.selectDateFrom = from
        var to = new Date()
        to.setHours(23)
        to.setMinutes(59)
        to.setSeconds(59)
        this.selectDateTo = to
      },
    }
  }
</script>