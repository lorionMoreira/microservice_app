const express = require("express");
const bodyParser = require("body-parser");
const axios = require("axios");

const app = express();
app.use(bodyParser.json());

app.post("/events", (req, res) => {
  const event = req.body;

  console.log(event)

  axios.post("http://localhost:8081/api/events", event).catch((err) => {//post
    console.log(err.message);
  });
  axios.post("http://localhost:8082/api/events", event).catch((err) => {//comment
    console.log(err.message);
  });
  axios.post("http://localhost:8083/api/events", event).catch((err) => {//query
    console.log(err.message);
  });
  res.send({ status: "OK" });
});

app.listen(4005, () => {
  console.log("Listening on 4005");
});
