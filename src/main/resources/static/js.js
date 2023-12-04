$(document).ready(function () {
        setInterval(
            function () {
                $.ajax({
                    type: "GET",
                    url: "/scan/table",
                    success: function (data) {},
                    dataType: "json"
                })
                $.ajax({
                    type: "GET",
                    url: "/scan/count",
                    success: function (data) {
                        console.log(data)
                        $('#first-info').text(`Количество = ${data}`)
                    }
                })
                $.ajax({
                    type: "GET",
                    url: "/scan/area",
                    success: function (data) {
                        console.log(data)
                        $('#second-info').text(`Площадь = ${data} `).append(`м<sup>2</sup>`)
                    }
                })
                $.ajax({
                    type: "GET",
                    url: "/scan/cur_id",
                    success: function (data) {
                        console.log(data)
                        $('#main-info').text(`Пирамида = ${data}`)
                    }
                })
            }, 200)
    }
)