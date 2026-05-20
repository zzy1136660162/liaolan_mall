import request from "@/utils/request.js";

/**
 * 关于辽缆 - 获取页面信息（无需登录）
 */
export function getAboutInfo() {
  return request.get("about/info", {}, { noAuth: true });
}

/**
 * 关于辽缆 - 获取完整页面信息（包含所有板块）
 */
export function getAboutFullInfo() {
  return request.get("about/info", {}, { noAuth: true });
}
