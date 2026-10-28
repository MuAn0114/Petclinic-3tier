-- Demo grooming bookings. Contact and pet details are placeholders because they were not provided.
INSERT IGNORE INTO grooming_bookings
  (id, guardian_name, phone, pet_name, pet_type, pet_weight, service, preferred_at, memo, status)
VALUES
  (1, '정현성', '미입력', '미정', '미정', NULL, '목욕', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00'), '샘플 예약', 'PENDING'),
  (2, '강한솔', '미입력', '미정', '미정', NULL, '부분 미용', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 2 DAY), '11:00:00'), '샘플 예약', 'PENDING'),
  (3, '이종무', '미입력', '미정', '미정', NULL, '전체 미용', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 3 DAY), '13:00:00'), '샘플 예약', 'PENDING'),
  (4, '신수정', '미입력', '미정', '미정', NULL, '고양이 케어', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 4 DAY), '14:00:00'), '샘플 예약', 'PENDING'),
  (5, '정인성', '미입력', '미정', '미정', NULL, '목욕', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 5 DAY), '15:00:00'), '샘플 예약', 'PENDING'),
  (6, '조현준', '미입력', '미정', '미정', NULL, '상담 후 결정', TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 6 DAY), '16:00:00'), '샘플 예약', 'PENDING');
