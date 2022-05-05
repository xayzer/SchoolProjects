function show_chart1(sp_num, ftr_num, ttg_num, mlp_num, wtp_num) {
    //TODO reset the labels
    const cartoonData = {
        labels: [
            'Winnie-the-Pooh',
            'South Park',
            'Futurama',
            'Teen Titans Go',
            'My Little Pony'
        ],
        datasets: [{
            data: [wtp_num, sp_num, ftr_num, ttg_num, mlp_num],
            backgroundColor: [
                'rgb(54, 12, 235)',
                'rgb(153, 12, 255)',
                'rgb(201, 203, 207)',
                'rgb(120, 245, 11)',
                'rgb(13, 12, 25)'
            ],
            hoverOffset: 4
        }]
    };

    const config2 = {
        type: 'doughnut',
        data: cartoonData,
        options: {
            radius: "90%",
            plugins: {
                title: {
                    display: true,
                    text: 'Cartoon',
                    font: {
                        size: 24
                    },
                    padding: 20
                },
                legend: {
                    display: true
                }

            },
        }
    };

    var myDoughchart = new Chart(
        document.getElementById('chart1'),
        config2
    );
}

function show_chart2(datachart2) {
    //console.log(datachart2);
    var chart_label = Object.keys(datachart2);
    var chart_data = Object.values(datachart2);
    console.log(chart_label);
    console.log(chart_data);

    const characterData = {
        labels: chart_label,
        datasets: [{
            backgroundColor: [
                'rgb(75, 192, 192)',
                'rgb(54, 162, 235)',
                'rgb(153, 102, 255)',
                'rgb(201, 203, 207)',
                'rgb(45, 33, 57)'
            ],
            borderColor: ['rgb(255, 99, 132)',
                'rgb(255, 159, 64)',
                'rgb(255, 205, 86)',
                'rgb(75, 192, 192)',
            ],
            data: chart_data
        }]
    };

    const config1 = {
        type: 'bar',
        data: characterData,
        options: {
            indexAxis: 'y',
            scales: {
                x: {
                    title: {
                        display: true,
                        text: "Numbers",
                        font: {
                            size: 12
                        },
                        padding: 10
                    },
                },
                y: {
                    title: {
                        display: true,
                        text: "Characters",
                        font: {
                            size: 12
                        },
                        padding: 10
                    },

                }

            },
            plugins: {

                title: {
                    display: true,
                    text: 'Characters',
                    font: {
                        size: 18
                    },
                    padding: 10
                },
                legend: {
                    display: false
                }
            },
        }

    }

    var myBarchart = new Chart(
        document.getElementById('chart2'),
        config1
    );

}

function show_chart3(datachart3) {
    console.log(datachart3);
    chart_label = Object.keys(datachart3[0]);
    chart_data1 = Object.values(datachart3[0]);
    chart_data2 = Object.values(datachart3[1]);
    const data = {
        labels: chart_label,
        datasets: [
            {
                label: 'AVG AGE',
                data: chart_data1,
                borderColor: ['rgb(54, 162, 235)']
            },
            {
                label: 'MAX AGE',
                data: chart_data2,
                borderColor: ['rgb(255, 159, 64)'],
            }
        ]
    };

    const config = {
        type: 'line',
        data: data,
        options: {
            responsive: true,
            scales: {
                x: {
                    title: {
                        display: true,
                        text: "Characters",
                        font: {
                            size: 16
                        },
                        padding: 8
                    },
                },
                y: {
                    title: {
                        display: true,
                        text: "Ages",
                    },
                    min: 0
                }

            },
            plugins: {
                legend: {
                    position: 'top',
                },
                title: {
                    display: true,
                    text: 'AVG Ages & Max Ages'
                }
            }
        },
    };

    var myLinechart = new Chart(
        document.getElementById('chart3'),
        config
    );
}