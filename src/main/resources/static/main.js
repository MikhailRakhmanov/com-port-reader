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
                    url: "/data",
                    success: function (data) {
                        let field = $('.content');
                        field.text("");
                        Object.keys(data).forEach(key => {
                            let resultsStr = `<tr id="${key}"><td>${key}</td>`
                            data[key].forEach(el => resultsStr += (`<td>${el}</td>`))
                            resultsStr+=(`</tr>`)
                            field.append(resultsStr)

                        })
                    },
                    dataType: "json"
                })
                $.ajax({
                    type: "GET",
                    url: "/start",
                    success: function (data) {
                        if (data) {
                            $(".button").css("display", "none")
                        } else {
                            $(".button").css("display", "flex")
                        }
                    },
                    dataType: "json"
                })
            }, 3000)
    }
)

function start() {
    $(document).ready(function () {
        $.ajax({
            type: "POST",
            url: "/start",
            data: ` `,
            success: function (result) {
            },
            dataType: "json"
        });
        $('.button').css("display", "none");
    });
}
