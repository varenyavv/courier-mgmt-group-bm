CREATE MATERIALIZED VIEW mv_pincode_city_state AS
    SELECT p.pincode AS pincode, city.name AS city, state.name AS state
    FROM pincode p, city, state
    WHERE p.city = city.city_id AND city.state = state.state_code
    ORDER BY p.pincode
WITH DATA;

CREATE INDEX mv_pincode_city_state_idx_01 ON mv_pincode_city_state(pincode);




