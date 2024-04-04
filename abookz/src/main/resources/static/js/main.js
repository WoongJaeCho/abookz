// header
let nav = document.querySelectorAll(".nav > ul > li");

nav.forEach(navItem=>{
    navItem.addEventListener("mouseover",function(){
        this.querySelector('.submenu').style.height = "350px";
    })
    navItem.addEventListener("mouseout",function(){
        this.querySelector('.submenu').style.height = "0px";
    })
})

// main
// slide
let currentIndex = 0;
let sliderWrap = document.querySelector('.sliderWrap');
let slider = document.querySelectorAll('.slider');
let sliderClone = sliderWrap.firstElementChild.cloneNode(true);
sliderWrap.append(sliderClone);

setInterval(()=>{
    currentIndex++;
    sliderWrap.style.marginLeft = - currentIndex * 100 + "%";
    sliderWrap.style.transition = "all 600ms";

    if(currentIndex == slider.length){
        setTimeout(()=>{
            sliderWrap.style.marginLeft = "0";
            sliderWrap.style.transition = "0s";
            currentIndex = 0;
        },700)
    }
},3000)