var toDevelopment = document.querySelector('#toDevelopment')
var development = document.querySelector('#development')

var toApartment = document.querySelector('#toApartment')
var quarter_apartment = document.querySelector('#quarter_apartment')

var toFacility = document.querySelector('#toFacility')
var quarter_facility = document.querySelector('#quarter_facility')

var toCarpark = document.querySelector('#toCarpark')
var quarter_carpark = document.querySelector('#quarter_carpark')

var toManagement = document.querySelector('#toManagement')
var management = document.querySelector('#management')

var toRepairform = document.querySelector('#toRepairform')
var repair_form = document.querySelector('#repair_form')

var frontBtns = document.getElementsByClassName('frontBtn');

var front = document.querySelector('#front')

function to(toEl) {
    // toEl 为指定跳转到该位置的DOM节点    
    let bridge = toEl;
    let body = document.body;
    let height = 0;
    // 计算该 DOM 节点到 body 顶部距离    
    do { height += bridge.offsetTop; bridge = bridge.offsetParent; }
    while (bridge !== body)
    // 滚动到指定位置    
    window.scrollTo({ top: height, behavior: 'smooth' })
}

toRepairform.addEventListener('click', function () { to(repair_form) })
toDevelopment.addEventListener('click', function () { to(development) });
toApartment.addEventListener('click', function () { to(quarter_apartment) });
toFacility.addEventListener('click', function () { to(quarter_facility) });
toCarpark.addEventListener('click', function () { to(quarter_carpark) });
toManagement.addEventListener('click', function () { to(management) });
for (var i in frontBtns) {
    frontBtns[i].addEventListener('click', function () { to(front) });
}








