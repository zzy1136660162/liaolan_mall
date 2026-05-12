<template>
  <div class="divBox">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>关于辽缆 - 内容管理</span>
        <span style="color: #909399; font-size: 12px; margin-left: 12px">共6个板块，点击编辑可修改内容</span>
      </div>

      <!-- 板块列表 -->
      <el-table v-loading="listLoading" :data="tableData" style="width: 100%" size="small" row-key="id">
        <el-table-column label="排序" prop="sort" width="70" align="center" />
        <el-table-column label="板块名称" min-width="120">
          <template slot-scope="{ row }">
            <el-tag type="info" size="small">{{ row.blockName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="板块标识" prop="blockKey" min-width="140" />
        <el-table-column label="主标题" prop="title" min-width="160" show-overflow-tooltip />
        <el-table-column label="副标题" prop="subTitle" min-width="140" show-overflow-tooltip />
        <el-table-column label="状态" width="80" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.status ? 'success' : 'danger'" size="mini">{{ row.status ? '启用' : '停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="更新时间" prop="updateTime" width="160" />
        <el-table-column label="操作" width="100" fixed="right">
          <template slot-scope="scope">
            <a @click="onEdit(scope.row)" v-hasPermi="['admin:about:block:update']">编辑</a>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑弹窗 -->
    <el-dialog
      :title="'编辑 - ' + editForm.blockName"
      :visible.sync="visible"
      width="720px"
      :close-on-click-modal="false"
      top="5vh"
    >
      <el-form :model="editForm" ref="editFormRef" label-width="100px" v-loading="editLoading">
        <el-form-item label="板块名称">
          <el-input v-model="editForm.blockName" disabled placeholder="板块名称" style="width: 50%" />
        </el-form-item>
        <el-form-item label="主标题">
          <el-input v-model="editForm.title" placeholder="请输入主标题" maxlength="255" />
        </el-form-item>
        <el-form-item label="副标题">
          <el-input v-model="editForm.subTitle" placeholder="请输入副标题" maxlength="255" />
        </el-form-item>

        <!-- 富文本编辑：公司介绍、生产实力、企业文化 -->
        <el-form-item label="内容" v-if="isRichTextBlock">
          <Tinymce v-model="editForm.content" :height="300" />
        </el-form-item>

        <!-- 发展历程：时间线编辑器 -->
        <el-form-item label="发展历程" v-if="editForm.blockKey === 'development_history'">
          <div class="timeline-editor">
            <div v-for="(item, index) in timelineData" :key="index" class="timeline-item">
              <el-input v-model="item.year" placeholder="年份" style="width: 100px" class="mr10" />
              <el-input v-model="item.event" placeholder="事件描述" style="width: 360px" class="mr10" />
              <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="removeTimeline(index)" />
            </div>
            <el-button type="primary" size="small" icon="el-icon-plus" @click="addTimeline" style="margin-top: 10px">
              添加时间节点
            </el-button>
          </div>
        </el-form-item>

        <!-- 资质荣誉：简短文本 + 多图上传 -->
        <el-form-item label="概述文本" v-if="editForm.blockKey === 'qualification_honor'">
          <el-input
            v-model="editForm.content"
            type="textarea"
            :rows="3"
            placeholder="请输入资质荣誉概述"
          />
        </el-form-item>

        <!-- 联系方式：结构化表单 -->
        <template v-if="editForm.blockKey === 'contact_info'">
          <el-form-item label="联系电话">
            <el-input v-model="contactData.phone" placeholder="如：400-xxx-xxxx" />
          </el-form-item>
          <el-form-item label="公司地址">
            <el-input v-model="contactData.address" placeholder="如：辽宁省xxx" />
          </el-form-item>
          <el-form-item label="电子邮箱">
            <el-input v-model="contactData.email" placeholder="如：info@xxx.com" />
          </el-form-item>
          <el-form-item label="地图链接">
            <el-input v-model="contactData.mapUrl" placeholder="高德/百度地图链接（H5点击跳转用）" />
          </el-form-item>
          <el-form-item label="纬度">
            <el-input v-model="contactData.latitude" placeholder="纬度（小程序导航用）" style="width: 200px" />
          </el-form-item>
          <el-form-item label="经度">
            <el-input v-model="contactData.longitude" placeholder="经度（小程序导航用）" style="width: 200px" />
          </el-form-item>
        </template>

        <!-- 图片上传：资质荣誉、生产实力、企业文化、公司介绍 -->
        <el-form-item label="图片" v-if="isImageBlock">
          <div class="image-uploader-wrap">
            <div v-for="(url, index) in imageData" :key="index" class="image-item">
              <el-image :src="url" fit="cover" style="width: 80px; height: 80px; border-radius: 4px" />
              <i class="el-icon-close image-del" @click="removeImage(index)" />
            </div>
            <div class="image-item image-add" @click="selectImage" v-if="imageData.length < 20">
              <i class="el-icon-plus" />
            </div>
          </div>
          <div style="color: #909399; font-size: 12px; margin-top: 6px">建议尺寸 750×500，最多20张</div>
        </el-form-item>

        <el-form-item label="排序值">
          <el-input-number v-model="editForm.sort" :min="0" :max="99" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="editForm.status" :active-value="true" :inactive-value="false" active-text="启用" inactive-text="停用" />
        </el-form-item>
      </el-form>

      <div slot="footer">
        <el-button @click="visible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 图片选择弹窗（复用项目已有的uploadPicture组件） -->
    <el-dialog :visible.sync="imageDialogVisible" title="选择图片" width="860px" append-to-body>
      <uploadPicture v-if="imageDialogVisible" @getImage="onImageSelected" />
    </el-dialog>
  </div>
</template>

<script>
import { aboutBlockListApi, aboutBlockInfoApi, aboutBlockUpdateApi } from '@/api/about';
import Tinymce from '@/components/Tinymce';
import uploadPicture from '@/components/uploadPicture';

export default {
  name: 'ContentAbout',
  components: { Tinymce, uploadPicture },
  data() {
    return {
      // 列表相关
      listLoading: false,
      tableData: [],

      // 编辑弹窗相关
      visible: false,
      editLoading: false,
      saveLoading: false,
      editForm: {
        id: null,
        blockKey: '',
        blockName: '',
        title: '',
        subTitle: '',
        content: '',
        imageUrls: '',
        sort: 0,
        status: true,
      },

      // 发展历程的时间线数据
      timelineData: [],

      // 联系方式的结构化数据
      contactData: {
        phone: '',
        address: '',
        email: '',
        mapUrl: '',
        latitude: '',
        longitude: '',
      },

      // 图片数据
      imageData: [],
      imageDialogVisible: false,
    };
  },
  computed: {
    /** 需要富文本编辑器的板块 */
    isRichTextBlock() {
      return ['company_intro', 'production_strength', 'company_culture'].includes(this.editForm.blockKey);
    },
    /** 需要图片上传的板块 */
    isImageBlock() {
      return ['company_intro', 'qualification_honor', 'production_strength', 'company_culture'].includes(this.editForm.blockKey);
    },
  },
  mounted() {
    this.getList();
  },
  methods: {
    /** 获取列表 */
    getList() {
      this.listLoading = true;
      aboutBlockListApi()
        .then((res) => {
          this.tableData = res || [];
          this.listLoading = false;
        })
        .catch(() => {
          this.listLoading = false;
        });
    },

    /** 点击编辑按钮 */
    onEdit(row) {
      this.editLoading = true;
      this.visible = true;

      aboutBlockInfoApi(row.id)
        .then((res) => {
          this.editForm = {
            id: res.id,
            blockKey: res.blockKey,
            blockName: res.blockName,
            title: res.title || '',
            subTitle: res.subTitle || '',
            content: res.content || '',
            imageUrls: res.imageUrls || '',
            sort: res.sort || 0,
            status: res.status !== false,
          };

          // 根据板块类型，解析特殊字段
          this.parseBlockContent(res);

          this.editLoading = false;
        })
        .catch(() => {
          this.editLoading = false;
          this.visible = false;
        });
    },

    /** 解析不同板块的特殊 content / imageUrls */
    parseBlockContent(res) {
      // 发展历程 → 解析时间线 JSON
      if (res.blockKey === 'development_history') {
        try {
          this.timelineData = res.content ? JSON.parse(res.content) : [];
        } catch {
          this.timelineData = [];
        }
      }

      // 联系方式 → 解析结构化 JSON
      if (res.blockKey === 'contact_info') {
        try {
          const data = res.content ? JSON.parse(res.content) : {};
          this.contactData = {
            phone: data.phone || '',
            address: data.address || '',
            email: data.email || '',
            mapUrl: data.mapUrl || '',
            latitude: data.latitude || '',
            longitude: data.longitude || '',
          };
        } catch {
          this.contactData = { phone: '', address: '', email: '', mapUrl: '', latitude: '', longitude: '' };
        }
      }

      // 图片 → 解析 JSON 数组
      try {
        this.imageData = res.imageUrls ? JSON.parse(res.imageUrls) : [];
      } catch {
        this.imageData = [];
      }
    },

    /** 保存 */
    handleSave() {
      this.saveLoading = true;

      const postData = {
        id: this.editForm.id,
        title: this.editForm.title,
        subTitle: this.editForm.subTitle,
        sort: this.editForm.sort,
        status: this.editForm.status,
      };

      // 发展历程 → 把时间线数组转成 JSON
      if (this.editForm.blockKey === 'development_history') {
        postData.content = JSON.stringify(this.timelineData.filter((item) => item.year || item.event));
      } else if (this.editForm.blockKey === 'contact_info') {
        // 联系方式 → 把结构化数据转成 JSON
        postData.content = JSON.stringify(this.contactData);
      } else {
        // 其他板块直接用 content
        postData.content = this.editForm.content;
      }

      // 图片 → 转成 JSON 数组
      if (this.isImageBlock) {
        postData.imageUrls = JSON.stringify(this.imageData);
      } else {
        postData.imageUrls = this.editForm.imageUrls || '';
      }

      aboutBlockUpdateApi(postData)
        .then(() => {
          this.$message.success('保存成功');
          this.visible = false;
          this.getList();
          this.saveLoading = false;
        })
        .catch(() => {
          this.saveLoading = false;
        });
    },

    // ===== 发展历程时间线操作 =====
    addTimeline() {
      this.timelineData.push({ year: '', event: '' });
    },
    removeTimeline(index) {
      this.timelineData.splice(index, 1);
    },

    // ===== 图片操作 =====
    selectImage() {
      this.imageDialogVisible = true;
    },
    onImageSelected(imgList) {
      // uploadPicture组件回调的是数组，每项的sattDir是图片URL
      if (imgList && imgList.length) {
        imgList.forEach((item) => {
          if (item.sattDir) {
            this.imageData.push(item.sattDir);
          }
        });
      }
      this.imageDialogVisible = false;
    },
    removeImage(index) {
      this.imageData.splice(index, 1);
    },
  },
};
</script>

<style scoped lang="scss">
.timeline-editor {
  width: 100%;
  .timeline-item {
    display: flex;
    align-items: center;
    margin-bottom: 8px;
  }
}

.image-uploader-wrap {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;

  .image-item {
    position: relative;
    width: 80px;
    height: 80px;
    border-radius: 4px;
    overflow: hidden;

    .image-del {
      position: absolute;
      top: 2px;
      right: 2px;
      width: 18px;
      height: 18px;
      line-height: 18px;
      text-align: center;
      background: rgba(0, 0, 0, 0.5);
      color: #fff;
      border-radius: 50%;
      cursor: pointer;
      font-size: 10px;
    }
  }

  .image-add {
    display: flex;
    align-items: center;
    justify-content: center;
    border: 1px dashed #d9d9d9;
    border-radius: 4px;
    cursor: pointer;
    font-size: 24px;
    color: #8c939d;

    &:hover {
      border-color: #409eff;
      color: #409eff;
    }
  }
}
</style>
