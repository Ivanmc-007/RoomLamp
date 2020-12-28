
var roomApi = Vue.resource('/room{/id}');
var stompClient = null;

function connect() {
    var socket = new SockJS("/gs-guide-websocket");
    stompClient = Stomp.over(socket);
    // отключить debug (засоряет консоль)
    stompClient.debug = () => {};
    stompClient.connect({}, frame => {
        console.log("***Connected ON MAIM*** "+ frame);
        stompClient.subscribe("/topic/rooms", message => {
            if(message.body) {
                let roomFromServer = JSON.parse(message.body);
                app.rooms.push(roomFromServer);
            }
        });
    });
}

function sendRoom(room) {
    console.log("***SEND_ROOM***");
    // отправка room на сервер
    stompClient.send("/app/room", {}, JSON.stringify(room));
}

connect();

var app = new Vue({
  el: "#app",
  data: {
    rooms: [],
    roomName: "",
    roomCountry: ""
  },
  methods: {
    saveRoom() {
        let room = {
            roomName: this.roomName,
            roomCountry: this.roomCountry
        };
        sendRoom(room);
    }
  },
  created: function () {
      roomApi.get().then(response => response.json().then(data => {
        data.forEach(room => this.rooms.push(room));
      }));
  }
});
