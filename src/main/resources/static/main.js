const ip = "192.168.1.146";
$(document).ready(
    function () {
        let currentLength = 0;
        let box = $('.content');
        setInterval(
            function () {
                if (currentLength !== 0) {
                    $('.button').css("display", "none");
                }
                $.ajax({
                    type: "GET",
                    url: "http://".concat(ip).concat(":8080/data"),
                    headers: {  'Access-Control-Allow-Origin': "http://".concat(ip).concat(":8080/data") },
                    success: function (data) {
                        console.log(data.length)
                        if (currentLength !== data.length) {
                            box.text('')
                            data.forEach(e => {
                                box.append(e + '<br>')
                            })
                            currentLength = data.length
                        }
                    },
                    dataType: "json"
                })
            }, 1)
    }
)

function start() {
    $(document).ready(function () {
        $.ajax({
            type: "POST",
            url: "http://".concat(ip).concat(":8080/start"),
            headers: {  'Access-Control-Allow-Origin': "http://".concat(ip).concat(":8080/start") },
            data: ` `,
            success: function (result) {
            },
            dataType: "json"
        });
        $('.button').css("display", "none");
    });
}
