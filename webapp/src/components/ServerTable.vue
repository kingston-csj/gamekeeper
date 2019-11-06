<style type="text/css">
  .server_table_footer {
    display: flex;
    box-sizing: content-box;
    padding-top: 10px;
    padding-bottom: 0px;
    margin-bottom: 0px;
    justify-content: space-between;
  }
</style>
<template>
  <div>
    <div style="display: flex;justify-content: flex-start">
      <el-input
        placeholder="通过服务器id搜索..."
        prefix-icon="el-icon-search"
        v-model="keywords" style="width: 400px" size="mini">
      </el-input>
      <el-button type="primary" icon="el-icon-search" size="mini" style="margin-left: 3px" @click="searchClick">搜索
      </el-button>
    </div>
  
    <el-table
      ref="multipleTable"
      :data="servers"
      tooltip-effect="dark"
      style="width: 100%;overflow-x: hidden; overflow-y: hidden;" v-loading="loading">
      <el-table-column
        type="selection"
        width="35" align="left" v-if="showEdit || showDelete">
      </el-table-column>
      <el-table-column
        label="服务器名称"
        width="200" align="left">
        <template slot-scope="scope"><span style="color: #409eff;cursor: pointer" @click="itemClick(scope.row)">{{ scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="服务器ip" width="140" align="left">
        <template slot-scope="scope">{{ scope.row.ip}}</template>
      </el-table-column>
      <el-table-column
        prop="onlinePlayerSum"
        label="在线人数"
        width="120" align="left">
      </el-table-column>
      <el-table-column
        prop="cachePlayerSum"
        label="缓存人数"
        width="120" align="left">
      </el-table-column>
    </el-table>
    <div class="server_table_footer">
    </div>
  </div>
</template>

<script>
  import {httpGet} from '../utils/httpclient'


  export default{
    data() {
      return {
        servers: [],
        loading: false,
        currentPage: 1,
        totalCount: -1,
        pageSize: 10,
        keywords: '',
      }
    },
    mounted: function () {
      var _this = this;
      this.loading = true;
      this.loadRecords(1, this.pageSize);
      var _this = this;
      window.bus.$on('tableReload', function () {
        _this.loading = true;
        _this.loadRecords(_this.currentPage, _this.pageSize);
      })
    },
    methods: {
      searchClick(){
        this.loadRecords(1, this.pageSize);
      },
      //翻页
      currentChange(currentPage){
        this.currentPage = currentPage;
        this.loading = true;
        this.loadRecords(currentPage, this.pageSize);
      },
      loadRecords(page, count){
        var _this = this;
        var url = "/server/all" + "?page=" + page + "&count=" + count ; 
        
        httpGet(url).then(resp=> {
          _this.loading = false;
          if (resp.status == 200) {
            _this.servers = resp.data.servers;
            console.info("_this.servers=="+_this.servers)
            _this.totalCount = resp.data.totalCount;
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
    },
  }
</script>
