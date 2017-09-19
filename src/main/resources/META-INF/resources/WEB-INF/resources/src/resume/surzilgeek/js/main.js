/* Table of Contents
==================================================

# Sticky Nav

==================================================
*/

    
// -------------------------------------------------------------
//  Sticky Nav
// -------------------------------------------------------------
(function () {  
    function menuToggle(){
        var windowWidth = $(window).width();
        if(windowWidth > 991 ){
            $(window).on('scroll', function(){
                if( $(window).scrollTop()>735 ){
                    $('.home-two .navbar').addClass('navbar-fixed-top animated fadeInDown');
                } else {
                    $('.home-two .navbar').removeClass('navbar-fixed-top animated fadeInDown');
                };
            });
        }else{
            
            $('.home-two .navbar').addClass('navbar-fixed-top');
                
        };  
    }

    menuToggle();
}()); 



