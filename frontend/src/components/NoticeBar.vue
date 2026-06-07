<script setup lang="ts">
import { ref, computed, onMounted } from "vue";
import type { Notice } from "../types";

const props = defineProps<{
  notices: Notice[];
}>();

const collapsed = ref(false);
const closedIds = ref<number[]>([]);

onMounted(() => {
  try {
    const saved = localStorage.getItem("notice_closed_ids");
    if (saved) {
      closedIds.value = JSON.parse(saved);
    }
    const collapsedState = localStorage.getItem("notice_collapsed");
    if (collapsedState === "true") {
      collapsed.value = true;
    }
  } catch (e) {
    // ignore
  }
});

const visibleNotices = computed(() => {
  return props.notices.filter((n) => !closedIds.value.includes(n.id));
});

const urgentNotices = computed(() => {
  return visibleNotices.value.filter((n) => n.level === "URGENT");
});

const normalNotices = computed(() => {
  return visibleNotices.value.filter((n) => n.level === "NORMAL");
});

const hasNotices = computed(() => visibleNotices.value.length > 0);
const hasUrgent = computed(() => urgentNotices.value.length > 0);
const hasNormal = computed(() => normalNotices.value.length > 0);

function closeNotice(id: number) {
  closedIds.value.push(id);
  try {
    localStorage.setItem("notice_closed_ids", JSON.stringify(closedIds.value));
  } catch (e) {
    // ignore
  }
}

function toggleCollapse() {
  collapsed.value = !collapsed.value;
  try {
    localStorage.setItem("notice_collapsed", String(collapsed.value));
  } catch (e) {
    // ignore
  }
}

function formatDate(dateStr: string) {
  const date = new Date(dateStr);
  const month = date.getMonth() + 1;
  const day = date.getDate();
  const hours = date.getHours().toString().padStart(2, "0");
  const minutes = date.getMinutes().toString().padStart(2, "0");
  return `${month}月${day}日 ${hours}:${minutes}`;
}
</script>

<template>
  <section v-if="hasNotices" class="notice-bar">
    <div v-if="hasUrgent" class="notice-section urgent-section">
      <div class="notice-header urgent-header">
        <div class="notice-title">
          <span class="urgent-badge">紧急</span>
          <span class="notice-count">{{ urgentNotices.length }} 条重要公告</span>
        </div>
        <div class="notice-actions">
          <button v-if="hasNormal" class="collapse-btn" @click="toggleCollapse">
            {{ collapsed ? "展开全部" : "收起普通" }}
          </button>
        </div>
      </div>
      <div class="notice-list">
        <article
          v-for="notice in urgentNotices"
          :key="notice.id"
          class="notice-item urgent-item"
        >
          <div class="notice-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z" />
              <line x1="12" y1="9" x2="12" y2="13" />
              <line x1="12" y1="17" x2="12.01" y2="17" />
            </svg>
          </div>
          <div class="notice-content">
            <h4 class="notice-item-title">{{ notice.title }}</h4>
            <p class="notice-item-desc">{{ notice.content }}</p>
            <span class="notice-time">{{ formatDate(notice.createdAt) }}</span>
          </div>
          <button class="close-btn" @click="closeNotice(notice.id)" title="关闭">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18" />
              <line x1="6" y1="6" x2="18" y2="18" />
            </svg>
          </button>
        </article>
      </div>
    </div>

    <div v-if="hasNormal && !collapsed" class="notice-section normal-section">
      <div class="notice-header normal-header">
        <div class="notice-title">
          <span class="normal-badge">通知</span>
          <span class="notice-count">{{ normalNotices.length }} 条门店通知</span>
        </div>
      </div>
      <div class="notice-list">
        <article
          v-for="notice in normalNotices"
          :key="notice.id"
          class="notice-item normal-item"
        >
          <div class="notice-icon normal-icon">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" />
              <path d="M13.73 21a2 2 0 0 1-3.46 0" />
            </svg>
          </div>
          <div class="notice-content">
            <h4 class="notice-item-title">{{ notice.title }}</h4>
            <p class="notice-item-desc">{{ notice.content }}</p>
            <span class="notice-time">{{ formatDate(notice.createdAt) }}</span>
          </div>
          <button class="close-btn" @click="closeNotice(notice.id)" title="关闭">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="6" x2="6" y2="18" />
              <line x1="6" y1="6" x2="18" y2="18" />
            </svg>
          </button>
        </article>
      </div>
    </div>

    <div v-if="hasNormal && collapsed && !hasUrgent" class="notice-collapsed">
      <span class="collapsed-text">
        还有 <strong>{{ normalNotices.length }}</strong> 条通知
      </span>
      <button class="expand-btn" @click="toggleCollapse">
        展开查看
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <polyline points="6 9 12 15 18 9" />
        </svg>
      </button>
    </div>
  </section>
</template>

<style scoped>
.notice-bar {
  width: min(1180px, calc(100vw - 32px));
  margin: 0 auto 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.notice-section {
  border-radius: 8px;
  border: 1px solid color-mix(in srgb, #19212e 13%, transparent);
  background: color-mix(in srgb, #f4f7fb 86%, white 14%);
  box-shadow: 0 18px 50px color-mix(in srgb, #19212e 10%, transparent);
  overflow: hidden;
}

.urgent-section {
  border-color: color-mix(in srgb, #cf5c36 40%, transparent);
  background: linear-gradient(
    135deg,
    color-mix(in srgb, #cf5c36 8%, #f4f7fb),
    color-mix(in srgb, #cf5c36 4%, #f4f7fb)
  );
}

.notice-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 20px;
  border-bottom: 1px solid color-mix(in srgb, #19212e 10%, transparent);
}

.urgent-header {
  background: color-mix(in srgb, #cf5c36 10%, transparent);
  border-bottom-color: color-mix(in srgb, #cf5c36 20%, transparent);
}

.normal-header {
  background: color-mix(in srgb, #3268b8 8%, transparent);
  border-bottom-color: color-mix(in srgb, #3268b8 15%, transparent);
}

.notice-title {
  display: flex;
  align-items: center;
  gap: 10px;
}

.urgent-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  border-radius: 6px;
  background: #cf5c36;
  color: white;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.normal-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  border-radius: 6px;
  background: #3268b8;
  color: white;
  font-size: 12px;
  font-weight: 700;
  letter-spacing: 0.5px;
}

.notice-count {
  font-size: 14px;
  font-weight: 600;
  color: #19212e;
}

.notice-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.collapse-btn {
  padding: 6px 12px;
  border: 1px solid color-mix(in srgb, #19212e 20%, transparent);
  border-radius: 6px;
  background: transparent;
  color: #19212e;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.collapse-btn:hover {
  background: color-mix(in srgb, #19212e 8%, transparent);
  border-color: color-mix(in srgb, #19212e 30%, transparent);
}

.notice-list {
  display: flex;
  flex-direction: column;
}

.notice-item {
  display: flex;
  align-items: flex-start;
  gap: 14px;
  padding: 16px 20px;
  border-bottom: 1px solid color-mix(in srgb, #19212e 8%, transparent);
  transition: background 0.2s ease;
}

.notice-item:last-child {
  border-bottom: none;
}

.notice-item:hover {
  background: color-mix(in srgb, #19212e 4%, transparent);
}

.urgent-item:hover {
  background: color-mix(in srgb, #cf5c36 6%, transparent);
}

.notice-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 8px;
  flex-shrink: 0;
}

.urgent-item .notice-icon {
  background: color-mix(in srgb, #cf5c36 15%, transparent);
  color: #cf5c36;
}

.normal-icon {
  background: color-mix(in srgb, #3268b8 15%, transparent);
  color: #3268b8;
}

.notice-content {
  flex: 1;
  min-width: 0;
}

.notice-item-title {
  margin: 0 0 6px;
  font-size: 15px;
  font-weight: 700;
  color: #19212e;
  line-height: 1.4;
}

.urgent-item .notice-item-title {
  color: #cf5c36;
}

.notice-item-desc {
  margin: 0 0 8px;
  font-size: 14px;
  line-height: 1.6;
  color: color-mix(in srgb, #19212e 75%, transparent);
}

.notice-time {
  font-size: 12px;
  color: color-mix(in srgb, #19212e 55%, transparent);
}

.close-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 28px;
  height: 28px;
  border: none;
  border-radius: 6px;
  background: transparent;
  color: color-mix(in srgb, #19212e 45%, transparent);
  cursor: pointer;
  transition: all 0.2s ease;
  flex-shrink: 0;
}

.close-btn:hover {
  background: color-mix(in srgb, #19212e 10%, transparent);
  color: #19212e;
}

.urgent-item .close-btn:hover {
  background: color-mix(in srgb, #cf5c36 15%, transparent);
  color: #cf5c36;
}

.notice-collapsed {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  padding: 14px 20px;
  border-radius: 8px;
  border: 1px dashed color-mix(in srgb, #3268b8 30%, transparent);
  background: color-mix(in srgb, #3268b8 6%, transparent);
}

.collapsed-text {
  font-size: 14px;
  color: color-mix(in srgb, #19212e 70%, transparent);
}

.collapsed-text strong {
  color: #3268b8;
  font-weight: 700;
}

.expand-btn {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 6px 14px;
  border: none;
  border-radius: 6px;
  background: #3268b8;
  color: white;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.expand-btn:hover {
  background: color-mix(in srgb, #3268b8 85%, #19212e);
  transform: translateY(-1px);
}

@media (max-width: 640px) {
  .notice-bar {
    width: calc(100vw - 32px);
  }

  .notice-item {
    padding: 14px 16px;
    gap: 12px;
  }

  .notice-header {
    padding: 12px 16px;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .notice-icon {
    width: 32px;
    height: 32px;
  }
}
</style>
