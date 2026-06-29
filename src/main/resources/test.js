import http from 'k6/http';

export const options = { vus: 50, iterations: 50 };

export default function () {
  http.post('http://localhost:8080/api/v1/orders', JSON.stringify({
    eventId: "5b6bc185-eea9-4072-ba71-67b5fc453af3",
    seatCount: 1,
    email: `user${__VU}@example.com`
  }), { headers: { 'Content-Type': 'application/json' } });
}