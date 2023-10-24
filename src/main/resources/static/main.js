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
                    url: "/data",
                    success: function (data) {
                        let field = $('.content');
                        field.text("");
                        Object.keys(data).forEach(key => {
                            let resultsStr = `<tr id="${key}"><td><b>${key}</b></td>`
                            data[key].forEach(el => resultsStr += (`<td>${el}</td>`))
                            resultsStr += (`</tr>`)
                            field.append(resultsStr)

                        })
                    },
                    dataType: "json"
                })
                $.ajax({
                    type: "GET",
                    url: "/start",
                    success: function (isStart) {
                        let startButton = $(".start")
                        let barcode = $('.show-barcode')
                        if (isStart) {
                            startButton.css("display", "none")
                            barcode.css("display", 'block')
                        } else {
                            startButton.css("display", "flex")
                            barcode.css("display", 'none')
                        }
                    },
                    dataType: "json"
                })
            }, 50)
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
        $('.start').css("display", "none");
    });
}

function showBarCode() {
    if ($(".show-barcode").text() !== "Скрыть штрих код") {
        $('.barcode').css("display", 'block');
        $('.show-barcode').text("Скрыть штрих код")
    }else {
        $('.barcode').css("display", 'none');
        $('.show-barcode').text("Показать штрих код")
    }
}

function CallPrint(strid) {
    let part = $(`#${strid}`)
    let WinPrint = window.open('','','left=50,top=50,width=800,height=640,toolbar=0,scrollbars=1,status=0');

    WinPrint.document.write('<link rel="stylesheet" href="/style.css">');
    WinPrint.document.write(part.html());
    WinPrint.document.close();
    WinPrint.focus();
    WinPrint.print();
}