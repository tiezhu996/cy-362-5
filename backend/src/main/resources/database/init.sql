CREATE TABLE IF NOT EXISTS operation_records (
  id SERIAL PRIMARY KEY,
  module_name VARCHAR(120) NOT NULL,
  owner_name VARCHAR(80) NOT NULL,
  status VARCHAR(40) NOT NULL,
  metric VARCHAR(40) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO operation_records (module_name, owner_name, status, metric)
VALUES ('剧本库与DM管理', '运营组', 'ready', '100%');

CREATE TABLE IF NOT EXISTS notices (
  id SERIAL PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  level VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  sort_order INTEGER NOT NULL DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO notices (title, content, level, is_active, sort_order) VALUES
('【紧急】6月10日系统升级维护通知', '为提升系统稳定性，将于2026年6月10日凌晨2:00-4:00进行系统升级维护，期间暂停所有服务，请提前做好安排。', 'URGENT', TRUE, 1),
('端午节假日营业安排', '6月10日-6月12日端午节期间，门店正常营业，营业时间调整为10:00-24:00，请各DM注意排班。', 'NORMAL', TRUE, 2),
('新剧本《迷雾山庄》上线', '新剧本《迷雾山庄》已录入系统，欢迎各位DM预约体验，首周预约享8折优惠。', 'NORMAL', TRUE, 3);
