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
SELECT
    host_id,
    hostname as host_name,
    to_timestamp((extract("epoch" FROM "public".host_usage."timestamp")::int / 300) * 300) as "timestamp",
    AVG(FLOOR((total_mem - memory_free/1000)*100/total_mem)) AS avg_used_mem_percentage
FROM "public".host_usage JOIN "public".host_info
    ON "public".host_usage.host_id = "public".host_info.id
GROUP BY
    host_id,
    hostname,
    to_timestamp((extract("epoch" FROM "public".host_usage."timestamp")::int / 300) * 300)
ORDER BY "timestamp" ASC;


-- detect host failure. Shows all host_id where number of responses is less than 3
SELECT host_id, host_name, "timestamp", COUNT(host_id) as num_data_points
FROM (
    SELECT
        host_id,
        host_name,
        "timestamp"
    FROM (
        SELECT
            host_id,
            hostname AS host_name,
            to_timestamp((extract("epoch" FROM "public".host_usage."timestamp")::int / 300) * 300) as "timestamp"
        FROM
            "public".host_usage JOIN "public".host_info
        ON
            "public".host_usage.host_id = "public".host_info.id
    ) AS ss) tt
GROUP BY host_id, host_name, "timestamp"
HAVING COUNT(host_id) < 3
ORDER BY "timestamp" ASC;