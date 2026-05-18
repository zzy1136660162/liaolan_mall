<template>
  <div class="divBox">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>关于辽缆 - 内容管理</span>
        <span style="color: #909399; font-size: 12px; margin-left: 12px">共11个板块，点击编辑可修改内容</span>
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

        <!-- ===== Hero横幅：背景图上传 ===== -->
        <template v-if="editForm.blockKey === 'hero_banner'">
          <el-form-item label="背景图片">
            <div class="image-uploader-wrap">
              <div v-for="(url, index) in imageData" :key="index" class="image-item">
                <el-image :src="url" fit="cover" style="width: 80px; height: 80px; border-radius: 4px">
                  <div slot="error" class="image-error-slot">
                    <i class="el-icon-picture-outline" />
                  </div>
                </el-image>
                <i class="el-icon-close image-del" @click="removeImage(index)" />
              </div>
              <div class="image-item image-add" @click="selectImage" v-if="imageData.length < 1">
                <i class="el-icon-picture" />
                <span class="add-text">素材库</span>
              </div>
              <el-upload
                v-if="imageData.length < 1"
                class="image-item image-upload-btn"
                action="#"
                :http-request="handleUploadRequest"
                :show-file-list="false"
                :before-upload="handleBeforeUpload"
                accept="image/*"
              >
                <i class="el-icon-plus" />
                <span class="add-text">本地上传</span>
              </el-upload>
            </div>
            <div style="color: #909399; font-size: 12px; margin-top: 6px">建议尺寸 750×500，仅1张背景图</div>
          </el-form-item>
        </template>

        <!-- ===== 企业统计：动态列表 ===== -->
        <template v-if="editForm.blockKey === 'company_stats'">
          <el-form-item label="统计项">
            <div class="dynamic-list">
              <div v-for="(item, index) in statsData" :key="index" class="dynamic-item">
                <el-input v-model="item.value" placeholder="数值（如：50万㎡）" style="width: 120px" class="mr10" />
                <el-input v-model="item.label" placeholder="标签（如：现代化厂区）" style="width: 200px" class="mr10" />
                <el-select v-model="item.icon" placeholder="图标" style="width: 130px" class="mr10">
                  <el-option label="工厂" value="factory" />
                  <el-option label="工程师" value="engineering" />
                  <el-option label="认证" value="verified" />
                  <el-option label="精密制造" value="precision_manufacturing" />
                </el-select>
                <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="statsData.splice(index, 1)" />
              </div>
              <el-button type="primary" size="small" icon="el-icon-plus" @click="statsData.push({ value: '', label: '', icon: 'factory' })" style="margin-top: 10px">
                添加统计项
              </el-button>
            </div>
          </el-form-item>
        </template>

        <!-- ===== 核心优势：动态列表 ===== -->
        <template v-if="editForm.blockKey === 'core_advantages'">
          <el-form-item label="优势项">
            <div class="dynamic-list">
              <div v-for="(item, index) in advantagesData" :key="index" class="dynamic-item advantages-item">
                <div class="adv-row">
                  <el-input v-model="item.title" placeholder="优势标题" style="width: 200px" class="mr10" />
                  <el-select v-model="item.icon" placeholder="图标" style="width: 130px" class="mr10">
                    <el-option label="认证" value="verified" />
                    <el-option label="精密制造" value="precision_manufacturing" />
                    <el-option label="握手" value="handshake" />
                  </el-select>
                  <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="advantagesData.splice(index, 1)" />
                </div>
                <el-input v-model="item.desc" type="textarea" :rows="2" placeholder="优势描述" style="margin-top: 8px" />
              </div>
              <el-button type="primary" size="small" icon="el-icon-plus" @click="advantagesData.push({ title: '', desc: '', icon: 'verified' })" style="margin-top: 10px">
                添加优势项
              </el-button>
            </div>
          </el-form-item>
        </template>

        <!-- ===== 领导致辞：结构化表单 ===== -->
        <template v-if="editForm.blockKey === 'leader_speech'">
          <el-form-item label="致辞内容">
            <el-input v-model="leaderData.quote" type="textarea" :rows="4" placeholder="请输入领导致辞内容" />
          </el-form-item>
          <el-form-item label="姓名">
            <el-input v-model="leaderData.name" placeholder="如：张建国" style="width: 50%" />
          </el-form-item>
          <el-form-item label="职务">
            <el-input v-model="leaderData.title" placeholder="如：董事长兼首席执行官" style="width: 50%" />
          </el-form-item>
          <el-form-item label="头像">
            <div class="image-uploader-wrap">
              <div v-if="leaderData.avatar" class="image-item">
                <el-image :src="leaderData.avatar" fit="cover" style="width: 80px; height: 80px; border-radius: 4px">
                  <div slot="error" class="image-error-slot">
                    <i class="el-icon-picture-outline" />
                  </div>
                </el-image>
                <i class="el-icon-close image-del" @click="leaderData.avatar = ''" />
              </div>
              <div v-else class="image-item image-add" @click="selectLeaderAvatar">
                <i class="el-icon-picture" />
                <span class="add-text">素材库</span>
              </div>
              <el-upload
                v-if="!leaderData.avatar"
                class="image-item image-upload-btn"
                action="#"
                :http-request="(param) => handleUploadRequest(param, 'leader')"
                :show-file-list="false"
                :before-upload="handleBeforeUpload"
                accept="image/*"
              >
                <i class="el-icon-plus" />
                <span class="add-text">本地上传</span>
              </el-upload>
            </div>
            <div style="color: #909399; font-size: 12px; margin-top: 6px">建议正方形头像</div>
          </el-form-item>
        </template>

        <!-- ===== 核心团队：动态列表 ===== -->
        <template v-if="editForm.blockKey === 'core_team'">
          <el-form-item label="团队成员">
            <div class="dynamic-list">
              <div v-for="(item, index) in teamData" :key="index" class="dynamic-item team-member-item">
                <div class="team-row">
                  <el-input v-model="item.name" placeholder="姓名" style="width: 120px" class="mr10" />
                  <el-input v-model="item.title" placeholder="职务" style="width: 180px" class="mr10" />
                  <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="teamData.splice(index, 1)" />
                </div>
                <div class="team-avatar-row">
                  <div v-if="item.avatar" class="team-avatar-preview">
                    <el-image :src="item.avatar" fit="cover" style="width: 48px; height: 48px; border-radius: 4px">
                      <div slot="error" class="image-error-slot-sm">
                        <i class="el-icon-picture-outline" />
                      </div>
                    </el-image>
                    <i class="el-icon-close team-avatar-del" @click="item.avatar = ''" />
                  </div>
                  <el-button size="mini" @click="selectTeamAvatar(index)">素材库</el-button>
                  <el-upload
                    class="team-avatar-upload"
                    action="#"
                    :http-request="(param) => handleUploadRequest(param, 'team', index)"
                    :show-file-list="false"
                    :before-upload="handleBeforeUpload"
                    accept="image/*"
                  >
                    <el-button size="mini">本地上传</el-button>
                  </el-upload>
                </div>
              </div>
              <el-button type="primary" size="small" icon="el-icon-plus" @click="teamData.push({ name: '', title: '', avatar: '' })" style="margin-top: 10px">
                添加成员
              </el-button>
            </div>
          </el-form-item>
        </template>

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

        <!-- 资质荣誉：结构化编辑（文本+标签） -->
        <template v-if="editForm.blockKey === 'qualification_honor'">
          <el-form-item label="概述文本">
            <el-input
              v-model="honorData.text"
              type="textarea"
              :rows="3"
              placeholder="请输入资质荣誉概述"
            />
          </el-form-item>
          <el-form-item label="荣誉标签">
            <div class="dynamic-list">
              <div v-for="(tag, index) in honorData.tags" :key="index" class="dynamic-item" style="align-items: center">
                <el-input v-model="honorData.tags[index]" placeholder="标签名称" style="width: 240px" class="mr10" />
                <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="honorData.tags.splice(index, 1)" />
              </div>
              <el-button type="primary" size="small" icon="el-icon-plus" @click="honorData.tags.push('')" style="margin-top: 10px">
                添加标签
              </el-button>
            </div>
          </el-form-item>
        </template>

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
              <el-image :src="url" fit="cover" style="width: 80px; height: 80px; border-radius: 4px">
                <div slot="error" class="image-error-slot">
                  <i class="el-icon-picture-outline" />
                </div>
              </el-image>
              <i class="el-icon-close image-del" @click="removeImage(index)" />
            </div>
            <div class="image-item image-add" @click="selectImage" v-if="imageData.length < 20">
              <i class="el-icon-picture" />
              <span class="add-text">素材库</span>
            </div>
            <el-upload
              v-if="imageData.length < 20"
              class="image-item image-upload-btn"
              action="#"
              :http-request="handleUploadRequest"
              :show-file-list="false"
              :before-upload="handleBeforeUpload"
              accept="image/*"
            >
              <i class="el-icon-plus" />
              <span class="add-text">本地上传</span>
            </el-upload>
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
import { fileImageApi } from '@/api/systemSetting';
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

      // 企业统计数据
      statsData: [],

      // 核心优势数据
      advantagesData: [],

      // 领导致辞数据
      leaderData: {
        quote: '',
        name: '',
        title: '',
        avatar: '',
      },

      // 核心团队数据
      teamData: [],

      // 资质荣誉结构化数据
      honorData: {
        text: '',
        tags: [],
      },

      // 图片数据
      imageData: [],
      imageDialogVisible: false,
      // 用于区分图片选择器的目标（通用/领导头像/团队头像）
      imageSelectTarget: 'general',
      teamAvatarIndex: -1,
    };
  },
  computed: {
    /** 需要富文本编辑器的板块 */
    isRichTextBlock() {
      return ['company_intro', 'production_strength', 'company_culture'].includes(this.editForm.blockKey);
    },
    /** 需要图片上传的板块（通用多图） */
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

      // 企业统计 → 解析 JSON 数组
      if (res.blockKey === 'company_stats') {
        try {
          this.statsData = res.content ? JSON.parse(res.content) : [];
        } catch {
          this.statsData = [];
        }
      }

      // 核心优势 → 解析 JSON 数组
      if (res.blockKey === 'core_advantages') {
        try {
          this.advantagesData = res.content ? JSON.parse(res.content) : [];
        } catch {
          this.advantagesData = [];
        }
      }

      // 领导致辞 → 解析 JSON 对象
      if (res.blockKey === 'leader_speech') {
        try {
          const data = res.content ? JSON.parse(res.content) : {};
          this.leaderData = {
            quote: data.quote || '',
            name: data.name || '',
            title: data.title || '',
            avatar: this.buildFullImageUrl(this.stripUploadPrefix(data.avatar || '')),
          };
        } catch {
          this.leaderData = { quote: '', name: '', title: '', avatar: '' };
        }
      }

      // 核心团队 → 解析 JSON 数组
      if (res.blockKey === 'core_team') {
        try {
          const rawTeam = res.content ? JSON.parse(res.content) : [];
          this.teamData = rawTeam.map((item) => ({
            ...item,
            avatar: this.buildFullImageUrl(this.stripUploadPrefix(item.avatar || '')),
          }));
        } catch {
          this.teamData = [];
        }
      }

      // 资质荣誉 → 解析 JSON 对象 { text, tags }
      if (res.blockKey === 'qualification_honor') {
        try {
          const data = res.content ? JSON.parse(res.content) : {};
          if (typeof data === 'string') {
            // 兼容：如果 content 是纯字符串（被旧版编辑器破坏的），则作为 text
            this.honorData = { text: data, tags: [] };
          } else {
            this.honorData = {
              text: data.text || '',
              tags: Array.isArray(data.tags) ? data.tags : [],
            };
          }
        } catch {
          // content 无法解析为 JSON，当作纯文本
          this.honorData = { text: res.content || '', tags: [] };
        }
      }

      // 图片 → 解析 JSON 数组（通用多图和 hero_banner 背景图）
      // 后端 ResponseRouter.prefixImage 会自动加 CDN 前缀，如果数据库存的是完整 URL 则会出现双重前缀
      // 此处修复：剥离所有 CDN 前缀后，再用 buildFullImageUrl 加上唯一正确的前缀
      try {
        const rawUrls = res.imageUrls ? JSON.parse(res.imageUrls) : [];
        this.imageData = rawUrls.map((url) => this.buildFullImageUrl(this.stripUploadPrefix(url)));
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

      // 根据板块类型，序列化 content
      switch (this.editForm.blockKey) {
        case 'development_history':
          postData.content = JSON.stringify(this.timelineData.filter((item) => item.year || item.event));
          break;
        case 'contact_info':
          postData.content = JSON.stringify(this.contactData);
          break;
        case 'company_stats':
          postData.content = JSON.stringify(this.statsData.filter((item) => item.value || item.label));
          break;
        case 'core_advantages':
          postData.content = JSON.stringify(this.advantagesData.filter((item) => item.title || item.desc));
          break;
        case 'leader_speech':
          postData.content = JSON.stringify({
            quote: this.leaderData.quote,
            name: this.leaderData.name,
            title: this.leaderData.title,
            avatar: this.stripUploadPrefix(this.leaderData.avatar),
          });
          break;
        case 'core_team':
          postData.content = JSON.stringify(
            this.teamData.filter((item) => item.name || item.title).map((item) => ({
              ...item,
              avatar: this.stripUploadPrefix(item.avatar),
            }))
          );
          break;
        case 'qualification_honor':
          // 资质荣誉：保存为 JSON { text, tags }
          postData.content = JSON.stringify({
            text: this.honorData.text,
            tags: this.honorData.tags.filter((t) => t && t.trim()),
          });
          break;
        default:
          // 其他板块直接用 content（富文本等）
          postData.content = this.editForm.content;
      }

      // 图片 → 转成 JSON 数组（剥离 CDN 前缀，存相对路径）
      if (this.isImageBlock || this.editForm.blockKey === 'hero_banner') {
        const relativeUrls = this.imageData.map((url) => this.stripUploadPrefix(url));
        postData.imageUrls = JSON.stringify(relativeUrls);
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
      this.imageSelectTarget = 'general';
      this.imageDialogVisible = true;
    },
    selectLeaderAvatar() {
      this.imageSelectTarget = 'leader';
      this.imageDialogVisible = true;
    },
    selectTeamAvatar(index) {
      this.imageSelectTarget = 'team';
      this.teamAvatarIndex = index;
      this.imageDialogVisible = true;
    },
    onImageSelected(imgList) {
      if (imgList && imgList.length) {
        imgList.forEach((item) => {
          if (item.sattDir) {
            if (this.imageSelectTarget === 'leader') {
              this.leaderData.avatar = item.sattDir;
            } else if (this.imageSelectTarget === 'team') {
              if (this.teamAvatarIndex >= 0 && this.teamAvatarIndex < this.teamData.length) {
                this.teamData[this.teamAvatarIndex].avatar = item.sattDir;
              }
            } else {
              this.imageData.push(item.sattDir);
            }
          }
        });
      }
      this.imageDialogVisible = false;
    },
    removeImage(index) {
      this.imageData.splice(index, 1);
    },

    // ===== 本地上传相关方法 =====
    handleBeforeUpload(file) {
      const isImage = file.type.startsWith('image/');
      const isLt5M = file.size / 1024 / 1024 < 5;
      if (!isImage) {
        this.$message.error('只能上传图片文件！');
        return false;
      }
      if (!isLt5M) {
        this.$message.error('图片大小不能超过 5MB！');
        return false;
      }
      return true;
    },

    /** 构建上传图片的完整访问 URL（与素材库返回的 sattDir 格式一致） */
    buildFullImageUrl(relativeUrl) {
      if (!relativeUrl) return '';
      // 已经是完整 URL，直接返回
      if (relativeUrl.startsWith('http://') || relativeUrl.startsWith('https://')) return relativeUrl;
      // 拼接后端地址：VUE_APP_UPLOAD_URL + / + 相对路径
      const base = (process.env.VUE_APP_UPLOAD_URL || '').replace(/\/+$/, '');
      return base + '/' + relativeUrl;
    },

    /**
     * 剥离 URL 中的 CDN 前缀，还原为相对路径
     * 后端 ResponseRouter.prefixImage 会在返回时自动加前缀，
     * 所以数据库只应存相对路径（如 crmebimage/public/about/...）
     * 循环剥离，兼容已存在的双重/多重前缀，以及 /http:// 前缀
     */
    stripUploadPrefix(url) {
      if (!url) return '';
      const base = (process.env.VUE_APP_UPLOAD_URL || '').replace(/\/+$/, '');
      let result = url;
      // 处理 /http://127.0.0.1:20500/crmebimage/... 格式（prefixImage 可能产生 /前缀）
      while (result.startsWith('/' + base + '/')) {
        result = result.substring(1);
      }
      // 处理 http://127.0.0.1:20500/http://127.0.0.1:20500/crmebimage/... 双重前缀
      while (base && result.startsWith(base + '/')) {
        result = result.substring(base.length + 1);
      }
      return result;
    },

    /** 本地上传：与 uploadPicture 组件使用相同的 fileImageApi */
    handleUploadRequest(param, target, teamIndex) {
      const formData = new FormData();
      formData.append('multipart', param.file);
      const params = { model: 'about', pid: 0 };

      fileImageApi(formData, params)
        .then((res) => {
          // fileImageApi 返回的 url 是相对路径，如 crmebimage/public/about/2026/05/18/xxx.png
          const imageUrl = this.buildFullImageUrl(res.url);
          if (!imageUrl) {
            this.$message.error('上传成功但未获取到图片地址');
            return;
          }
          if (target === 'leader') {
            this.leaderData.avatar = imageUrl;
          } else if (target === 'team' && teamIndex >= 0 && teamIndex < this.teamData.length) {
            this.teamData[teamIndex].avatar = imageUrl;
          } else {
            this.imageData.push(imageUrl);
          }
          this.$message.success('图片上传成功');
        })
        .catch((err) => {
          console.error('Upload error:', err);
          this.$message.error('图片上传失败，请确认后端已启动且 imagePath 配置正确');
        });
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

.dynamic-list {
  width: 100%;
  .dynamic-item {
    margin-bottom: 12px;
  }
  .advantages-item {
    padding: 12px;
    background: #fafafa;
    border-radius: 4px;
    border: 1px solid #ebeef5;
  }
  .adv-row, .team-row {
    display: flex;
    align-items: center;
  }
  .team-member-item {
    padding: 12px;
    background: #fafafa;
    border-radius: 4px;
    border: 1px solid #ebeef5;
  }
  .team-avatar-row {
    display: flex;
    align-items: center;
    margin-top: 8px;
    gap: 8px;
  }
  .team-avatar-preview {
    position: relative;
    .team-avatar-del {
      position: absolute;
      top: -4px;
      right: -4px;
      width: 16px;
      height: 16px;
      line-height: 16px;
      text-align: center;
      background: rgba(0, 0, 0, 0.5);
      color: #fff;
      border-radius: 50%;
      cursor: pointer;
      font-size: 10px;
    }
  }
  .team-avatar-upload {
    display: inline-block;
  }
}

.mr10 { margin-right: 10px; }

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

  .image-add,
  .image-upload-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border: 1px dashed #d9d9d9;
    border-radius: 4px;
    cursor: pointer;
    font-size: 20px;
    color: #8c939d;
    transition: all 0.3s;

    &:hover {
      border-color: #409eff;
      color: #409eff;
    }

    .add-text {
      font-size: 12px;
      margin-top: 4px;
    }
  }

  .image-upload-btn {
    background-color: #fafafa;

    &:hover {
      border-color: #67c23a;
      color: #67c23a;
      background-color: #f0f9eb;
    }
  }

  .image-add {
    &:hover {
      border-color: #409eff;
      color: #409eff;
    }
  }

  .image-error-slot {
    width: 80px;
    height: 80px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f5f7fa;
    color: #c0c4cc;
    font-size: 24px;
  }

  .image-error-slot-sm {
    width: 48px;
    height: 48px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f5f7fa;
    color: #c0c4cc;
    font-size: 18px;
  }
}
</style>
