<template>
  <div class="divBox">
    <el-card :bordered="false" shadow="never" class="ivu-mt" :body-style="{ padding: 0 }">
      <div class="padding-add">
        <el-form inline size="small">
          <el-form-item label="关键词：">
            <el-input v-model="listPram.keywords" placeholder="请输入关键词" class="selWidth" size="small" clearable>
            </el-input>
          </el-form-item>
          <el-form-item label="行业分类：">
            <el-select
              v-model="listPram.industryCategory"
              clearable
              class="selWidth"
              placeholder="请选择行业分类"
              @change="handerSearch"
            >
              <el-option v-for="item in industryOptions" :key="item" :label="item" :value="item" />
            </el-select>
          </el-form-item>
          <el-form-item label="区域分类：">
            <el-select
              v-model="listPram.regionCategory"
              clearable
              class="selWidth"
              placeholder="请选择区域分类"
              @change="handerSearch"
            >
              <el-option v-for="item in regionOptions" :key="item" :label="item" :value="item" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handerSearch" size="small">搜索</el-button>
            <el-button size="small" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="box-card mt14">
      <div slot="header" class="clearfix">
        <router-link :to="{ path: '/content/caseCreat' }">
          <el-button type="primary" class="mr10" v-hasPermi="['admin:case:save']">添加案例</el-button>
        </router-link>
      </div>
      <el-table v-loading="listLoading" :data="listData.list" size="mini" class="table" highlight-current-row>
        <el-table-column prop="id" label="ID" min-width="50" />
        <el-table-column label="图片" min-width="80">
          <template slot-scope="scope">
            <div class="demo-image__preview">
              <el-image
                style="width: 36px; height: 36px"
                :src="scope.row.imageInput"
                :preview-src-list="[scope.row.imageInput]"
              />
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="180" />
        <el-table-column prop="visit" label="浏览量" min-width="180">
          <template slot-scope="scope">
            <span>{{ scope.row.visit | filterEmpty }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="author" label="作者" min-width="180" />
        <el-table-column prop="synopsis" label="文章简介" show-overflow-tooltip min-width="180" />
        <el-table-column prop="projectName" label="项目名称" min-width="140">
          <template slot-scope="scope">
            <span>{{ scope.row.projectName | filterEmpty }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="industryCategory" label="行业分类" min-width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.industryCategory | filterEmpty }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="regionCategory" label="区域分类" min-width="100">
          <template slot-scope="scope">
            <span>{{ scope.row.regionCategory | filterEmpty }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="180" />
        <el-table-column label="操作" width="100" fixed="right">
          <template slot-scope="scope">
            <router-link :to="{ path: '/content/caseCreat/' + scope.row.id }">
              <a v-hasPermi="['admin:case:info']">编辑</a>
            </router-link>
            <el-divider direction="vertical"></el-divider>
            <a @click="handlerDelete(scope.row)" v-hasPermi="['admin:case:delete']">删除</a>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page="listPram.page"
        :page-sizes="constants.page.limit"
        :layout="constants.page.layout"
        :total="listData.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        background
      />
    </el-card>
  </div>
</template>

<script>
import * as caseApi from '@/api/case.js';
import * as selfUtil from '@/utils/ZBKJIutil.js';
export default {
  data() {
    return {
      constants: this.$constants,
      listPram: {
        keywords: null,
        page: 1,
        limit: this.$constants.page.limit[0],
        industryCategory: null,
        regionCategory: null,
      },
      industryOptions: ['能源领域', '建筑行业', '轨道交通', '电信通讯', '石油化工', '矿冶钢铁', '工业制造', '其他'],
      regionOptions: ['华东地区', '华南地区', '华北地区', '华中地区', '西南地区', '西北地区', '东北地区', '海外'],
      listData: { list: [], total: 0 },
      listLoading: true,
    };
  },
  mounted() {
    this.handlerGetListData(this.listPram);
  },
  methods: {
    handleReset() {
      this.listPram.keywords = null;
      this.listPram.industryCategory = null;
      this.listPram.regionCategory = null;
      this.handlerGetListData(this.listPram);
    },
    handerSearch() {
      this.listPram.page = 1;
      this.handlerGetListData(this.listPram);
    },
    handlerGetListData(pram) {
      this.listLoading = true;
      caseApi.getCaseList(pram).then((data) => {
        this.listData = data;
        this.listLoading = false;
      });
    },
    handlerDelete(rowData) {
      this.$modalSure('删除当前数据').then((result) => {
        caseApi.deleteCase(rowData).then((data) => {
          this.$message.success('删除数据成功');
          this.handlerGetListData(this.listPram);
        });
      });
    },
    handleSizeChange(val) {
      this.listPram.limit = val;
      this.handlerGetListData(this.listPram);
    },
    handleCurrentChange(val) {
      this.listPram.page = val;
      this.handlerGetListData(this.listPram);
    },
  },
};
</script>

<style scoped lang="scss">
</style>
