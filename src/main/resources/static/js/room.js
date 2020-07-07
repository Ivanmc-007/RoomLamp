var roomApi = Vue.resource('/room{/id}');
var router = new VueRouter({
    mode: 'history',
    routes: []
});

function connect() {
    var socket = new SockJS("/room-lamp-websocket");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, frame => {
//        console.log("***Connected IN ROOM*** "+ frame);
        stompClient.subscribe("/topic/roomOne", message => {
            if(message.body) {
                console.log("*****");
                console.log(message);

                let roomFromServer = JSON.parse(message.body);
                if(roomFromServer.id == app2.room.id)
                    app2.room.lampOn = roomFromServer.lampOn;
            }
        });
    });
}

connect();

// что-то отправляет (какой-то объект)
function sendSomething(something) {
    // отправка something на сервер
    stompClient.send("/app/roomLamp", {}, JSON.stringify(something));
}

var app2 = new Vue({
    router,
    el: "#app-room",
    data: {
        room: {
            id:"",
            roomName: "",
            roomCountry:"",
            lampOn:""
        },
        statesLamp: ["off","on"]
    },
    methods: {
        clickLamp() {
            // вызываем метод для отправки на сервер
            sendSomething({id: this.room.id});
        }
    },
    created: function() {
        let roomId = this.$route.query.roomId;

        roomApi.get({id: roomId}).then(response => response.json().then(room => {
//            console.log(room);
            if(room) {
                this.room.id = room.id;
                this.room.roomName = room.roomName;
                this.room.roomCountry = room.roomCountry;
                this.room.lampOn = room.lampOn;
            }
        }));
    },
    computed: {
        stateLamp() {
            if(this.room.lampOn) {
                return this.statesLamp[1];
            } else {
                return this.statesLamp[0];
            }
        }
    }
});