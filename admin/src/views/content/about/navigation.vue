<template>
  <div class="divBox">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>快捷导航管理</span>
        <el-button size="small" type="primary" style="float: right" @click="onAdd">新增导航</el-button>
      </div>

      <el-table v-loading="listLoading" :data="tableData" style="width: 100%" size="small" row-key="id">
        <el-table-column label="排序" prop="sort" width="70" align="center" />
        <el-table-column label="导航标题" prop="title" min-width="150" />
        <el-table-column label="跳转路径" prop="content" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="80" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.status ? 'success' : 'danger'" size="mini">{{ row.status ? '启用' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <a @click="onEdit(scope.row)" style="margin-right: 10px">编辑</a>
            <a @click="onDelete(scope.row)" style="color: #f56c6c">删除</a>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px" :close-on-click-modal="false">
      <el-form :model="form" ref="formRef" label-width="100px" @submit.native.prevent>
        <el-form-item label="导航标题" required>
          <el-input v-model="form.title" placeholder="请输入导航标题" maxlength="50" />
        </el-form-item>
        <el-form-item label="跳转路径" required>
          <el-input v-model="form.content" placeholder="如：/pages/goods_cate/goods_cate" maxlength="255" />
        </el-form-item>
        <el-form-item label="排序值">
          <el-input-number v-model="form.sort" :min="0" :max="99" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="true" :inactive-value="false" active-text="启用" inactive-text="停用" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { aboutBlockListApi, aboutBlockInfoApi, aboutBlockUpdateApi } from '@/api/about';

export default {
  name: 'ContentAboutNav',
  data() {
    return {
      listLoading: false,
      tableData: [],
      dialogVisible: false,
      dialogTitle: '',
      saveLoading: false,
      form: {
        id: null,
        title: '',
        content: '',
        sort: 0,
        status: true,
      },
      isEdit: false,
    };
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.listLoading = true;
      aboutBlockListApi()
        .then((res) => {
          this.tableData = (res || []).filter((item) => item.blockKey === 'quick_nav');
          this.listLoading = false;
        })
        .catch(() => {
          this.listLoading = false;
        });
    },
    onAdd() {
      this.isEdit = false;
      this.dialogTitle = '新增快捷导航';
      this.form = { id: null, title: '', content: '', sort: 0, status: true };
      this.dialogVisible = true;
    },
    onEdit(row) {
      this.isEdit = true;
      this.dialogTitle = '编辑快捷导航';
      aboutBlockInfoApi(row.id)
        .then((res) => {
          this.form = {
            id: res.id,
            title: res.title || '',
            content: res.content || '',
            sort: res.sort || 0,
            status: res.status !== false,
          };
          this.dialogVisible = true;
        })
        .catch(() => {});
    },
    onDelete(row) {
      this.$confirm('确定删除该导航项吗？', '提示', { type: 'warning' })
        .then(() => {
          aboutBlockUpdateApi({
            id: row.id,
            title: row.title,
            subTitle: '',
            content: '',
            sort: 0,
            status: false,
            imageUrls: '',
          })
            .then(() => {
              this.$message.success('已停用（软删除）');
              this.getList();
            });
        });
    },
    handleSave() {
      if (!this.form.title) {
        this.$message.warning('请输入导航标题');
        return;
      }
      if (!this.form.content) {
        this.$message.warning('请输入跳转路径');
        return;
      }
      this.saveLoading = true;
      aboutBlockUpdateApi({
        id: this.form.id,
        title: this.form.title,
        subTitle: '',
        content: this.form.content,
        sort: this.form.sort,
        status: this.form.status,
        imageUrls: '',
      })
        .then(() => {
          this.$message.success('保存成功');
          this.dialogVisible = false;
          this.getList();
        })
        .finally(() => {
          this.saveLoading = false;
        });
    },
  },
};
</script>
