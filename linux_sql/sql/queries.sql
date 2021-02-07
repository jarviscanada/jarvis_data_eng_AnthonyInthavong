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
) AS ss ORDER BY cpu_number ASC, total_mem DESC
;