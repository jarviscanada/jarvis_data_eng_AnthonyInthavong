-- group hosts by CPI numbers and sort by memory size in descending order (within each cpu_number group)
SELECT cpu_number, host_id, total_mem FROM
(
    SELECT
        cpu_number,
        id AS host_id,
        total_mem,
        rank() OVER (PARTITION BY cpu_number ORDER BY total_mem DESC)
    FROM
        "public".host_info
) AS ss ORDER BY cpu_number ASC, total_mem DESC;

-- average used memory percentage in 5 minute intervals
SELECT DISTINCT ON ("timestamp") host_id, host_name, "timestamp", avg_used_mem_percentage
FROM (
    SELECT
        host_id,
        host_name,
        "timestamp",
        AVG(FLOOR((total_mem - memory_free)*100/total_mem))
            OVER (PARTITION BY "timestamp")
            AS avg_used_mem_percentage
    FROM (
        SELECT
            host_id,
            hostname AS host_name,
            to_timestamp((extract("epoch" FROM "public".host_usage."timestamp")::int / 300) * 300) as "timestamp",
            total_mem,
            memory_free
        FROM
            "public".host_usage JOIN "public".host_info
        ON
            "public".host_usage.host_id = "public".host_info.id
    ) AS ss

ORDER BY "timestamp" ASC) tt;