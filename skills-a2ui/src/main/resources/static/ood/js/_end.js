        if(!('Class' in window))window.Class=function(){return ood.Class.apply(ood.Class,arguments);};

    if (typeof module !== 'undefined' && typeof exports === 'object') {
        module.exports = ood;
    } else if (typeof define === 'function' && (define.amd || define.cmd)) {
        define(function() { return ood; });
    }
}).call(this || (typeof window !== 'undefined' ? window : global));