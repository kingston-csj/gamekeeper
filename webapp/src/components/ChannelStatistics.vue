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
      <el-table-column prop="channelCode" label="归属代理" width="180">
        <template slot-scope="scope">{{ scope.row.channelCode}}</template>
      </el-table-column>

      <el-table-column prop="money" label="累计金额" width="180">
        <template slot-scope="scope">{{ scope.row.money}}</template>
      </el-table-column>

    </el-table> 
     
  <el-form :inline="true" class="demo-form-inline">  
  <el-form-item label="总金额:">
      <el-input v-model="moneySum" style="width:128px"></el-input>
  </el-form-item>
  </el-form>
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

  import {httpGet} from '../utils/httpclient'
 
  export default {
    data() {
      return {
        tableData: [],
        moneySum:0,
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
        var url = "/channel/statistics" 
          + "?selectFrom=" + this.selectDateFrom.getTime() + "&selectTo=" + this.selectDateTo.getTime() ; 
        
        httpGet(url).then(resp=> {
          _this.loading = false;
          if (resp.status == 200) {
            _this.tableData = resp.data.orderGroups;
            _this.moneySum = resp.data.moneySum;
            _this.loading2 = false;
          } else {
            _this.$message({type: 'error', message: '数据加载失败!'});
          }
        }, resp=> {
          _this.loading = false;
          if (resp.response.status == 403) {
            _this.$message({type: 'error', message: resp.response.data});
          } else {
            _this.$message({type: 'error', message: '数据加载失败!'});
          }
        }).catch(resp=> {
          //压根没见到服务器
          _this.loading = false;
          _this.$message({type: 'error', message: '数据加载失败!'});
        })

      },
      //查询
      onSubmit() {
        this.loadData();
      },
      initTime() {
        var date = new Date()
        date.setDate(date.getDate());
        date.setHours(0)
        date.setMinutes(0)
        date.setSeconds(0)
        this.selectDateFrom = date
        this.selectDateTo = new Date()
      },
    }
  }
</script>