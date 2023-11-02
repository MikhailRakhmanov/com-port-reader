$(document).ready(
    function () {
        let currentLength = 0;
        setInterval(
            function () {
                if (currentLength !== 0) {
                    $('.button').css("display", "none");
                }
                $.ajax({
                    type: "GET",
                    url: "/table",
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
                $.ajax({
                    type: "GET",
                    url: "/platform",
                    success: function (data) {

                        $('caption').text(`Лист на пирамиду ${data}`)
                    }
                })

                let res_info1 = $(`.result-data1`)
                let res_info2 = $(`.result-data2`)
                $.ajax({
                    type: "GET",
                    url: "/area",
                    success: function (data) {
                        res_info1.text("")
                        res_info1.append(`Площадь = ${data} м<sup>2</sup><br>`)
                    }
                })


                $.ajax({
                    type: "GET",
                    url: "/count",
                    success: function (data) {
                        res_info2.text("")
                        res_info2.append(`Количество = ${data}<br>`)
                    }
                })

                $.ajax({
                    type: "GET",
                    url: "/start",
                    success: function (isStart) {
                        let startButton = $(".start")
                        let barcode = $('.show-barcode')
                        if (isStart) {

                            $(`.hidden-at-start`).css("display","block")
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
            url: "/start",
            data: ` `,
            success: function (result) {
            },
            dataType: "json"
        });

        $(`.hidden-at-start`).css("display","block")
        $('.start').css("display", "none");
    });
}

function showBarCode() {
    if ($(".show-barcode").text() !== "Скрыть штрих код") {
        $('.barcode').css("display", 'block');
        $('.show-barcode').text("Скрыть штрих код")
    } else {
        $('.barcode').css("display", 'none');
        $('.show-barcode').text("Показать штрих код")
    }
}

function CallPrint(strid) {
    let part = $(`#${strid}`)
    let WinPrint = window.open('', '', 'left=50,top=50,width=800,height=640,toolbar=0,scrollbars=1,status=0');

    WinPrint.document.write(`<style>
        table,tr,td {
        border: 1px solid black;
        border-spacing: 0px;
        border-collapse: collapse;
    }
    table{
        width: 95vw;
    }
    </style>`);
    WinPrint.document.write(part.html());
    WinPrint.document.close();
    WinPrint.focus();
    WinPrint.print();
    WinPrint.close();
}