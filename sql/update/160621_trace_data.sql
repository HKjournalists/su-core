ALTER TABLE `trace_data`
MODIFY COLUMN `organization`  bigint(20) UNSIGNED NULL AFTER `leaveDate`;

