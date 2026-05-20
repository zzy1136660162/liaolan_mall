<template>
  <div>
    <el-row v-if="!isDisabled">
      <el-col :span="24">
        <el-form-item label="商品详情：">
          <Tinymce v-model.trim="formValidate.content" :key="htmlKey"></Tinymce>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row v-else>
      <el-col :span="24">
        <el-form-item label="商品详情：">
          <span v-html="formValidate.content || '无'"></span>
        </el-form-item>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Tinymce from '@/components/Tinymce/index';

export default {
  name: 'ProductContent',
  components: { Tinymce },
  props: {
    formValidate: {
      type: Object,
      required: true,
    },
    isDisabled: {
      type: Boolean,
      default: false,
    },
    htmlKey: {
      type: Number,
      default: 0,
    },
  },
};
</script>
