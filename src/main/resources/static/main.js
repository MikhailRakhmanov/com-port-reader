$(document).ready(
    function () {
        setInterval(
            function () {
                $.ajax({
                    type: "GET",
                    url: "/scan/cur_id",
                    success: function (data) {
                        $('.list_info').text(`Лист на пирамиду ${data.toString()}`)
                    }
                })
                $.ajax({
                    type: "GET",
                    url: "/scan/table",
                    success: function (data) {
                        let field = $('.content');
                        field.text("");
                        field.append(" <tr>\n" +
                            "            <td><b>№</b></td>\n" +
                            "            <td><b>Наименование</b></td>\n" +
                            "            <td><b>Маркировка</b></td>\n" +
                            "            <td><b>№ договора</b></td>\n" +
                            "            <td><b>Размер</b></td>\n" +
                            "            <td><b>Заказчик</b></td>\n" +
                            "            <td><b>Площадь</b></td>\n" +
                            "            <td><b>Начало</b></td>\n" +
                            "            <td><b>Вывоз</b></td>\n" +
                            "        </tr>")
                        let i = 1;
                        data.forEach(dataRow => {
                            let curStr = "<tr>"
                            curStr += `<td><b>${i++}</b></td>`
                            Object.keys(dataRow).forEach(key => {
                                    if (key == 'DTS' || key == 'DTF') {
                                        curStr += `<td>${new Date((dataRow[key] - 1) * 24 * 60 * 60 * 1000 + new Date(1900, 0, 0, 0, 0, 0, 0).valueOf()).toLocaleDateString()}</td>`
                                    } else {
                                        curStr += `<td>${dataRow[key]}</td>`
                                    }
                                }
                            )
                            curStr += "</tr>"
                            field.append(`${curStr}`);
                        })
                    },
                    dataType: "json"
                })



                let res_info1 = $(`.result-data1`)
                let res_info2 = $(`.result-data2`)
                $.ajax({
                    type: "GET",
                    url: "/scan/area",
                    success: function (data) {
                        res_info1.text("")
                        res_info1.append(`Площадь = ${data} м<sup>2</sup><br>`)
                    }
                })

                $.ajax({
                    type: "GET",
                    url: "/scan/count",
                    success: function (data) {
                        res_info2.text("")
                        res_info2.append(`Количество = ${data}<br>`)
                    }
                })

                $.ajax({
                    type: "GET",
                    url: "/scan/start",
                    success: function (isStart) {
                        let startButton = $(".start")
                        let barcode = $('.show-barcode')
                        if (isStart) {

                            $(`#hidden-at-start`).css("display","block")
                            startButton.css("display", "none")
                            barcode.css("display", 'block')
                        } else {
                            startButton.css("display", "flex")
                            barcode.css("display", 'none')
                        }
                    },
                    dataType: "json"
                })


            }, 200)
    }
)

function start() {
    $(document).ready(function () {
        $.ajax({
            type: "POST",
            url: "scan/start",
            data: ` `,
            success: function (result) {
            },
            dataType: "json"
        });

        $(`.hidden-at-start`).css("display","block")
        $('.start').css("display", "none");
    });
}
