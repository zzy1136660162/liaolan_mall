<!--
 * @Author: From-wh from-wh@hotmail.com
 * @Date: 2023-03-09 15:45:51
 * @FilePath: /admin/src/layout/navMenu/subItem.vue
 * @Description:
-->
<template>
  <div>
    <template v-for="val in filteredChil">
      <el-submenu :index="val.path" :key="val.path" v-if="val.children && val.children.length > 0">
        <template slot="title">
          <i class="ivu-icon" :class="'el-icon-' + val.icon"></i>
          <span>{{ val.title }}</span>
        </template>
        <sub-item :chil="val.children" />
      </el-submenu>
      <template v-else>
        <el-menu-item :index="val.path" :key="val.path">
          <template>
            <i class="ivu-icon" :class="val.icon ? 'el-icon-' + val.icon : ''"></i>
            <span>{{ val.title }}</span>
          </template>
        </el-menu-item>
      </template>
    </template>
  </div>
</template>

<script>
export default {
  name: 'subItem',
  props: {
    chil: {
      type: Array,
      default() {
        return [];
      },
    },
  },
  computed: {
    filteredChil() {
      const seen = new Set();
      return this.chil.filter((item) => {
        // 过滤掉没有可导航路径的权限按钮
        if (!item.path || item.path.trim() === '') return false;
        const key = item.path + '|' + item.title;
        if (seen.has(key)) return false;
        seen.add(key);
        return true;
      });
    },
    // 关于辽缆不需要子菜单，直接点击跳转；子项在递归中已被上层过滤，这里保持兼容即可
  },
};
</script>
