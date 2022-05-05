document.addEventListener("DOMContentLoaded", function () {
    const injuryData = {
        labels: [
            '100 SQ.M.',
            '135 SQ.M.',
            '150 SQ.M.',
            '170 SQ.M.'
        ],
        datasets: [{
            data: [3, 28, 30, 7],
            backgroundColor: [
                'rgb(220, 20, 60)',
                'rgb(255, 105, 180)',
                'rgb(255, 127, 80)',
                'rgb(255, 250, 205)'
            ],
            hoverOffset: 4
        }]

    };

    const config2 = {
        type: 'doughnut',
        data: injuryData,
        options: {
            radius: "90%",
            plugins: {
                title: {
                    display: true,
                    text: 'Apartment Porpotion',
                    font: {
                        family: "'Lucida Handwriting', cursive",
                        size: 24
                    },
                    padding: 20
                },

                legend: {
                    display: true,
                    labels: {
                        // This more specific font property overrides the global property
                        font: {
                            family: "'Comic Sans MS', 'Comic Sans', cursive",
                            size: 8
                        }
                    }
                }

            },
        }
    };

    var myDoughchart = new Chart(
        document.getElementById('myChart'),
        config2
    );
});