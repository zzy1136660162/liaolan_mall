import request from "@/utils/request.js";

/**
 * 关于辽缆 - 获取页面信息（无需登录）
 */
export function getAboutInfo() {
  return request.get("about/info", {}, { noAuth: true });
}
