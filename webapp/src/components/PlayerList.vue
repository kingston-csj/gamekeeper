<template>
<div>


  <div>
    <el-select
      v-model="selected"
      placeholder="请选择服务器"
      :loading="loading">
      <el-option
        @click.native="searchClick(id)"
        v-for="item in servers"
        :key="item"
        :label="item"
        :value="item">
      </el-option>
    </el-select>
  </div>


  <div style="margin:10px 0 0 0;">
        <el-input type="text" v-model="queryParam" placeholder="昵称或id" style="width: 20%"></el-input>
        <el-button type="primary" @click.native.prevent="submitClick" style="width: 10%">查询</el-button>
 
  </div>


  <div style="margin:20px 0 0 0;">
      <el-table
      :data="tableData"
      border
      style="width: 100%">
      <el-table-column
        prop="date"
        label="日期"
        width="150"  v-if=""  style="display: none">
      </el-table-column>
      <el-table-column
        prop="name"
        label="姓名"
        width="120">
      </el-table-column>
      <el-table-column
        prop="province"
        label="等级"
        width="120">
      </el-table-column>
      <el-table-column
        prop="city"
        label="金币"
        width="120">
      </el-table-column>
      <el-table-column
        prop="address"
        label="银币"
        width="300">
      </el-table-column>
      <el-table-column
        prop="zip"
        label="邮编"
        width="120">
      </el-table-column>
      <el-table-column
        label="操作"
        width="100">
        <template slot-scope="scope">
          <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
          <el-button type="text" size="small">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

</div>
</template>

<script>
  export default {
    methods: {
      handleClick(row) {
        console.log(row);
      }
    },

    data() {
      return {
        tableData: [{
          date: '2016-05-03',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-02',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-04',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }, {
          date: '2016-05-01',
          name: '王小虎',
          province: '上海',
          city: '普陀区',
          address: '上海市普陀区金沙江路 1518 弄',
          zip: 200333
        }],
        queryParam:''
      }
    }
  }
</script>