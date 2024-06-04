<template>
  <div style="float:left;padding-top:50px;">
    <el-upload
      :limit="5"
      action=""
      :on-change="handleChange"
      accept=".jpg,.png,.jpeg"
      :auto-upload="false"
    >
    <template #trigger>
        <el-button type="primary">上传图片</el-button>
      </template>
    </el-upload>
<!-- 
    <el-table :data="picTable" style="width: 100%" size="medium">
        <el-table-column prop="picture" label=""  >
          <template slot-scope="scope">
            <img :src="scope.row.url" min-width="200" height="100"/>
          </template>
        </el-table-column>
        <el-table-column prop="name" label=""  >scope.row.name</el-table-column>
      </el-table> -->


    <div class="image-container">
      <div v-for="(pic, index) in picTable" :key="index">
            <img :src="pic.url"  >
              {{ pic.name }}
             
      </div>
    </div>

  </div>
  </template>
<script>
  import axios from 'axios';
  import Cookies from "js-cookie";
  export default {
    data() {
      return {
        picTable: []
      };
    },
    mounted() {
      this.loadPictures()
    },
    methods: {
      handleChange(file) {
        let token = Cookies.get('token')
        const formData = new FormData();
        formData.append('file', file.raw);
        const headers= {
            'Content-Type': 'multipart/form-data'
          }
        this.$api.oss.uploadPic(formData, headers).then((res) => {
          if (res.code == 200) {
            this.loadPictures()
          }
        })
      },
      loadPictures() {
        this.$api.oss.queryAllPictures().then((res) => {
          this.picTable = res.data
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