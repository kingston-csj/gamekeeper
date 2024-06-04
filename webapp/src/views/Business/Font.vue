<template>
  <div style="float:left;padding-top:50px;">
    <el-upload
      action=""
      :limit="5"
      :on-change="handleChange"
      accept=".ttf"
      :auto-upload="false"
    >
    <template #trigger>
        <el-button type="primary">上传字体</el-button>
      </template>
    </el-upload>

    <el-table :data="tableData" style="width: 100%" v-loading="loading2" element-loading-text="拼命加载中">
      <el-table-column prop="id" label="id" width="180">
        <template slot-scope="scope">{{ scope.row.id}}</template>
      </el-table-column>

      <el-table-column prop="name" label="name" width="180">
        <template slot-scope="scope">{{ scope.row.name}}</template>
      </el-table-column>

      <el-table-column prop="url" label="url" width="180">
        <template slot-scope="scope">{{ scope.row.url}}</template>
      </el-table-column>

    </el-table> 

  </div>
  </template>
<script>
  import axios from 'axios';
  import Cookies from "js-cookie";
  export default {
    data() {
      return {
        tableData: []
      };
    },
    mounted() {
      this.loadFonts()
    },
    methods: {
      handleChange(file) {
        let token = Cookies.get('token')
        const formData = new FormData();
        formData.append('file', file.raw);
        const headers= {
            'Content-Type': 'multipart/form-data'
          }
        this.$api.oss.uploadFont(formData, headers).then((res) => {
          if (res.code == 200) {
            this.loadFonts()
          }
        })
      },
      loadFonts() {
        this.$api.oss.queryAllFonts().then((res) => {
          this.tableData = res.data
           console.info("-=-queryAllPictures-----"+JSON.stringify(res))
        })
      }
    }  
  }
</script>

<style>

 

.image-container {
  display: grid;
  width: 1000px;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 10px; /* 根据需要设置间距 */
  /* border: 1px solid; */
  /* border-color: blue; */
}

.image-container img {
  width: 100%; /* 或者设置一个固定的宽度 */
  height: auto;
  border: 1px solid;
  border-color: blue;
}

</style>