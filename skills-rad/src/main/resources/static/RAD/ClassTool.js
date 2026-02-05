ood.Class('RAD.ClassTool',null,{
    Static:{
        _this:'____this____',
        getClassName:function(str, parent){
            var reg1=/^(\s*\/\*[^*@]*\*+([^\/][^*]*\*+)*\/\s*)|^(\s*\/\/[^\n]*\s*)/,
                reg12=/(\s*\/\*[^*@]*\*+([^\/][^*]*\*+)*\/\s*)$|(\s*\/\/[^\n]*\s*)$/,
                reg2=/(\{([^\{\}]*)\})|(\[([^\[\]]*)\])/,
                reg3=/\s*(((function\s*([\w$]+\s*)?\(\s*([\w$\s,]*)\s*\)\s*)?(\{([^\{\}]*)\}))|(\[([^\[\]]*)\]))/g,
                reg4=/^[\xEF\xBB\xBF\uFEFF\s]*(ood.)?Class\s*\(\s*(([\']([^\']+)[\'])|([\"]([^\"]+)[\"]))\s*\,\s*(([\']([^\']*)[\'])|([\"]([^\"]*)[\"])|(null)|(undefined))\s*\,1\s*\)\s*;?\s*$/,
                reg5=/^[\xEF\xBB\xBF\uFEFF\s]*(ood.)?Class\s*\(/,
                reg6=/^[a-zA-Z]+[\w\.]*[\w]+$/,
                reg7=/^\"[a-zA-Z]+[\w\.]*[\w]*\"$/,
                reg8=/^\'[a-zA-Z]+[\w\.]*[\w]*\'$/
                ;
            // clear top comments
            while(reg1.test(str))
                str = str.replace(reg1,'');
            // clear bottom comments
            while(reg12.test(str))
                str = str.replace(reg12,'');

            // clear string/reg
            var arr=[
            [/"(\\.|[^"\\\n])*"/,function(a){
                return reg7.test(a[0])?a[0]:'\"\"'
            }],
            [/'(\\.|[^'\\\n])*'/,function(a){
                return reg8.test(a[0])?a[0]:"\'\'"
            }],
            [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/,'null'],

            // clear comments
            [/\/\*[^*]*\*+([^\/][^*]*\*+)*\//,''],
            [/\/\/[^\n]*/,'']
            ];

            str = ood.replace(str, arr);

            // check "Class(" string
            if(!reg5.test(str))
                return false;
            // clear "{}", "[]" and "funciton(){}"
            while(reg2.test(str))
                str = str.replace(reg3, '1');
            // check "Class(.....)"
            if(reg4.test(str))
                str=str.replace(reg4,parent?'$7':'$2').replace(/[\'\"]/g,"");
            else
               return false;
            // check name space
            if(reg6.test(str))
                return str;
            else
                return false;
        },
        isJson:function(txt){
            var reg = new RegExp("^(\\s*\\/\\*[^*@]*\\*+([^\\/][^*]*\\*+)*\\/\\s*)|^(\\s*\\/\\/[^\\n]*\\s*)");
            while(reg.test(txt))
                txt = txt.replace(reg,'');
            return /^\s*(\{|\[|function)/.test(txt);
        },
        //get class object from a Class declare, include comments words
        getClassObject : function(str){
            str = str.slice(str.indexOf('{')+1, str.lastIndexOf('}'));
            var obj = eval('({'+str+'})');
            str=null;
            return obj;
        },
        getCodeFromStruct:function(o,sublayer){
            try{
                var self = arguments.callee, arr=[];
                if(o){
                    if(o.sub){
                        if(o.frame){
                            ood.each(o.sub,function(o,i){
                                if(!ood.isNull(o.comments))
                                    arr.push((o.comments ||'') + i + ':' + (o.code?o.code:o.sub?self.call(this,o,true):''));
                            },this);
                            return (sublayer?'':(o.comments ||'')) + o.frame.replace('*1', o.name||'').replace('*2', o.pname||'').replace('*3', arr.join(', ').replace(/\$/g,"\x01")).replace(/\x01/g, "$");
                        }else return '';
                    }else
                        return (o.code ||'').replace(/^[\r\n]*/, '');
                }else return '';
            }catch(e){ood.message(ood.getRes('RAD.classtool.err2'))+":"+String(e);return false}
        },
        parseSingleBlock:function(txt){
            try{
                var reg1 = new RegExp("^(\\s*\\/\\*[^*@]*\\*+([^\\/][^*]*\\*+)*\\/\\s*)|^(\\s*\\/\\/[^\\n]*\\s*)"),
                    reg2 = new RegExp("(\\s*\\/\\*[^*@]*\\*+([^\\/][^*]*\\*+)*\\/\\s*)|^(\\s*\\/\\/[^\\n]*\\s*)$"),
                    str,
                    comments,
                    code;

                while(reg2.test(txt))
                    txt = txt.replace(reg2,'');

                str = txt;
                while(reg1.test(str))
                    str = str.replace(reg1,'');

                str = str.replace(/\s*/,'');
                if(!str)return {comments:null, code:null};

                comments = '\n'+txt.replace(str,'');
                code = str.replace(/\s*$/,'');

                //comments/reg
                str = ood.replace(str, [
                    ["(^|\\n)\\s*\\/\\*[^*@]*\\*+([^\\/][^*]*\\*+)*\\/\\s*", ''],
                    ["\\n\\s*\\/\\/[^\\n]*\\s*", ''],
                    [/([^\w\x24\/\'\"*)\]\?:]\s*)(\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*)\s*([\)\}\]:,;\r\n])/.source,function(arg,i){
                        return str[i+1]+ str[i+5];
                    }]
                ]);

                code = code.replace(/([}\]])[^}\]]*$/,'$1');

                //check it's a single block
                //in '' or ""
                str = ood.replace(code, [
                    ["null|undefined|NaN", ''],
                    [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/,''],
                    [/-?(\d*\.?\d+|\d+\.?\d*)([eE][+-]?\d+|%)?\b/,''],
            		["'(\\\\.|[^'\\\\])*'", ''],
            		['"(\\\\.|[^"\\\\])*"', '']
                ]);

                while(/(\{([^\{\}]*)\})|(\[([^\[\]]*)\])/.test(str)){
                    str = str.replace(/\s*(((function\s*([\w$]+\s*)?\(\s*([\w$\s,]*)\s*\)\s*)?(\{([^\{\}]*)\}))|(\[([^\[\]]*)\]))/g, '');
                }
                if (ood.str.trim(str)!='') return false;

                return {comments: comments, code:code};
            }catch(e){ood.message(ood.getRes('RAD.classtool.err3')+":"+String(e));return false}
        },
        //get class struct from a Class declare, include comments words
        getClassStruct : function(str){
            try{
                str = ood.replace(str, [
                    [/(\r\n|\r)/g, "\n"],
                    [/( +)(\n)/g, "$2"],
                    [/\t/g, "    "]
                ]);

                //clear mash
                var t,
                index=1,
                index1=1,
                cache={},
                cache1={},
                result,
                result2,
                code = function(str,i) {
            		var ret = "#" + (index++) +"#";
            		cache[ret] = str[0];
            		return ret;
            	},
            	//special for regexp source string
                _code = function(str,i) {
                    var ret = "#" + (index++) +"#";
            		cache[ret] = str[i+2];
            		return str[i+1]+ret+str[i+5];
            	},
            	code1 = function(str) {
            		var ret = "`" + (index1++);
            		cache1[ret] = str[0];
            		return ret;
            	},
            	restore = function(str){
                	return str.replace(/#(\d+)#/g, function(m){
                		return cache[m];
                	});
                },
            	restore1 = function(str){
                	return str.replace(/`(\d+)/g, function(m){
                		return cache1[m];
                	});
                };

                str = ood.replace(str, [
                    ["(^|\\n)\\s*\\/\\*[^*@]*\\*+([^\\/][^*]*\\*+)*\\/[^\\n]*", code],
                    ["(^|\\n)\\s*\\/\\/[^\\n]*", code],
                    [/([^\w\x24\/\'\"*)\]\?:]\s*)(\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*)\s*([\)\}\]:,;\r\n])/g.source,_code]
                ]);

                str = ood.replace(str, [
            		["'(\\\\.|[^'\\\\])*'", code1],
            		['"(\\\\.|[^"\\\\])*"', code1]
                ]);

                var frame = str.replace(/(^[^{]*\{)\s*((.|[\r\n])*)([^\s])(\s*}[^}]*$)/,'$1*3$5').replace(/(#\d+#)+/g,''),
                    o = {sub:{}};

                if(t=str.match(/^((#\d+#)+)/))
                    o.comments = restore(t[0]);
                else o.comments = '';

                o.name = restore1(cache1['`1'].replace(/[\'\" ]*/g,''));
                
                var i2=str.indexOf(",", 1),
                    i21=str.indexOf("[", i2+1),
                    i22=str.indexOf("]", i2+1),
                    i3=str.indexOf(",", i2+1);
                if(i21<i3){
                    var str=restore(restore1(str.substring(i21,i22+1)));
                    o.pname = eval(str);
                    str=null;
                }else{
                    o.pname = str.substring(i2+1,i3);
                    o.pname = /[\'\"]/.test(o.pname) ? o.pname.replace(/[\'\" ]*/g,''): null;
                }

                o.frame = restore1(frame.replace(o.name,'*1').replace(o.pname,'*2'));

                str = str.slice(str.indexOf('{')+1, str.lastIndexOf('}'));

                result = o.sub;

                //get {}
                var index2=1,
                cache2={},
                code2 = function(str) {
            		var ret = "'~" + index2++ +"'";
            		cache2[ret] = str;
            		return ret;
            	},
                code3 = function(a,b,str) {
                    if(a.indexOf('~')!=-1)return a;

            		var ret = "'~" + index2++ +"'";
            		cache2[ret] = str;
            		return b + ret;
            	},
            	restore2 = function(str){
                    if(str.indexOf('~')==-1)return str;

                    str = cache2["'"+str+"'"];
                    while(/'~\d+'/.test(str))
                        str = str.replace(/'~\d+'/g, function(m){
                		    return cache2[m];
                	    });
                	return str;
                };
                       
                while(/(\{([^\{\}\[\]]*)\})|(\[([^\[\]\{\}]*)\])/.test(str)){
                    str = str.replace(/\s*(((function\s*([\w$]+\s*)?\(\s*([\w$\s,]*)\s*\)\s*)?(\{([^\{\}\[\]]*)\}))|(\[([^\[\]\{\}]*)\]))/g, code2);
                }
                if(/[\{\}\[\]]/.test(str)){
                    return false;
                }

                // handler any \s for comments
                str = ood.replace(str, ['\\s+', code]);

                //get comments first , 'Constructor', 'Initialize', 'Before', 'After', 'Instance', 'Static'
                str = str.replace(/((#\d+#)+)([\w]+)((#\d+#)*):/g, function(z,a,b,c){
                    result[c] = {comments: restore(a)};
                    return c+':';
                });
                str = str.replace(/(#\d+#)/g, '');
                
                var obj;
                try{
                    obj = eval('({' + str + '})');
                    str=null;
                }catch(e){
                    return false
                 }

                //get code of those
                ood.arr.each(['Constructor', 'Dependency', 'Initialize', 'Before', 'After','$End'],function(i){
                    if(obj[i]){
                        result[i] = result[i] || {};
                        result[i].code = restore(restore1(restore2(obj[i])));
                    }else
                        result[i] = {};
                    ood.arr.each(ood.toArr('code,comments'),function(j){
                        result[i][j] = ood.isDefined(result[i][j])?result[i][j]:null;
                    });
                });

                var obj2;
                ood.arr.each(['Instance', 'Static'],function(i){
                    if(obj[i]){
                        //for not function/{}/[] vars
                        var temp = cache2["'"+obj[i]+"'"];
                        var frame = temp.replace(/(^[^{]*\{)\s*((.|[\r\n])*)([^\s])(\s*}[^}]*$)/,'$1*3$5');
                        //delete the last comment
                        temp = temp.replace(/(#\d+#)*\s*(\})$/g, '$2');

                        temp = '(' + temp + ')';

                        // handler any \s for comments
                        temp = ood.replace(temp, ['\\s+', code]);

                        result[i] = result[i] || {};
                        result2 = result[i].sub = {};
                        result[i].frame = frame;

                        temp = temp.replace(/(:)([^,\}]+)/g, code3);
                        temp = restore1(temp);

                        //get comments first
                        temp = temp.replace(/((#\d+#)+)([\w]+)((#\d+#)*):/g, function(z,a,b,c){
                            result2[c] = {comments: restore(a)};
                            return c+':';
                        });
                        //for multi comments
                        temp = temp.replace(/(#\d+#)/g, '');

                        obj2 = eval(temp);
                        temp=null;
                        ood.each(obj2,function(o,j){
                            result2[j] = result2[j] || {};
                            result2[j].code = restore(restore1(restore2(o)));
                        });
                    }else
                        result[i] = {};
                    ood.arr.each(ood.toArr('code,comments,sub,frame'),function(j){
                        result[i][j] = ood.isDefined(result[i][j])?result[i][j]:null;
                    });
                });

                return o;
            }catch(e){
                return false;
            }
        },

        //get class struct from code text analysis
        getClassStruct2 : function(code, index){
            // first Class
            if((!ood.isSet(index)) || index<0 || index>=code.length)index=code.length;
            var ns=this,
                cache=ns._Cache2||(ns._Cache2={});
            if(cache[code+"^**^"+index])return cache[code+"^**^"+index];
            
            var rf=function(a){return ood.str.repeat(" ", a[0].length)};
            var ocode=code;
            // clear string/reg,comments
            code = ood.replace(code, [
                // simplify some
                [/\r\n?|\n/,'\n'],
                [/(\uFEFF|\xA0|[\t\x0B\f])/, rf],
                //protect "a"
                [/"([\w\$])?[\w\.]*"/,'$0'],
                [/'([\w\$])?[\w\.]*'/,"$0"],
                // clear string
                [/"(\\.|[^"\\\n])*"/,rf],
                [/'(\\.|[^'\\\n])*'/,rf],
                // clear reg
                [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/,rf],
                // clear comments
                [/\/\*[^*]*\*+([^\/][^*]*\*+)*\//,rf],
                [/\/\/[^\n]*/,rf]
                ]);
                
            // find the right class start pos
            var regoodCls = /\b(ood.)?Class\s*\(\s*["'\w\$\.]+\s*\,\s*["'\w\$\.]+\s*\,\s*\{/g,
                found,clsStartPos=-1,clsEnd,j=-1,codeStart;
            // find the first one
            if((found = regoodCls.exec(code))!=null){
                j=regoodCls.lastIndex;
                if(j>=index) {
                    if(clsStartPos==-1){
                        // Not in a Class
                        return null;
                    }
                }
                // maybe it's the start pos of class's code
                clsStartPos=j-found[0].length;
            };

            // find struct
            var len=code.length,
                pos=code.indexOf("{", clsStartPos)+1,
                clsCodeStart=pos,
                layer=1,
                i1=code.indexOf("(", clsStartPos+1),
                i2=code.indexOf(",", i1+1),
                i21=code.indexOf("[", i2+1),
                i22=code.indexOf("]", i2+1),
                i3=code.indexOf(",", i2+1),
                hash={},prevColonPos=-1,qMark=false,
                keyPattern=/((\b[\w\$][\w]*\b)|(\'[^']*\')|(\"[^"]*\"))\s*$/,
                lastOKPos=pos,
                c,tmpstr,tmpstr2,tmpResult,key,type,args,subcode,codefrom,codeto,layer1Key,spaceCount=0;

            hash.name = code.substring(i1+1,i2).replace(/[\'\" ]*/g,'');
            if(i21<i3){
                hash.pname = code.substring(i21,i22+1);
                try{
                    hash.pname = eval(hash.pname);
                }catch(e){
                    return null;
                }
                ood.arr.each(hash.pname,function(o){
                    if(o=='ood.Module')
                        hash.isModule=true;
                    else{
                        var t=ood.SC(o);
                        hash.isModule=t && t['ood.Module'];
                    }
                });
            }else{
                hash.pname = code.substring(i2+1,i3);
                hash.pname = /[\'\"]/.test(hash.pname) ? hash.pname.replace(/[\'\" ]*/g,''): null;
                if(!/^[\w\$][\w\.]*$/.test(hash.name) || !/^[\w\$]?[\w\.]*$/.test(hash.pname)){
                    return null;
                }
                if(hash.pname=='ood.Module')
                    hash.isModule=true;
                else{
                    if(hash.pname){
                        var t=ood.SC(hash.pname);
                        hash.isModule=t && t['ood.Module'];
                    }
                }
            }

            hash.arr = [];

            // find sub object/functions
            do{
                c=code[pos++];
                //ignore boolean?{}:{} pattern;
                if(/\s/.test(c)){
                    spaceCount++;
                }else{
                    if(c=="?"){
                        if(layer<=2){
                            qMark=true;
                        }
                    }else if(c==":"){
                        if(layer<=2){
                            if(qMark){
                                qMark=false;
                                continue;
                            }else if(prevColonPos!=-1){
                                continue;
                            }else{
                                prevColonPos=pos;
                            }
                        }
                    }else if(c=="("){
                        // has prev :
                        if(prevColonPos!=-1){
                            var nn=code.indexOf(")", pos);
                            if(nn!=-1){
                                pos=nn+1;
                                continue;
                            }
                        }
                    }
                    else if(c==','){
                        // has prev : [same1]
                        if(prevColonPos!=-1){
                            // only collection top 2 layers
                            if(layer<=3){
                                type='other';
                                subcode=ocode.substring(codefrom=prevColonPos,codeto=pos-spaceCount-1);
                            }
                            
                            // get key
                            tmpstr2=code.substring(lastOKPos, prevColonPos-1);
                            tmpResult=keyPattern.exec(tmpstr2);
                            if(tmpResult!=null){
                                key=tmpResult[0].replace(/[\'\"\s]/g,'');    
                            }

                            // use once only
                            prevColonPos=-1;
                            lastOKPos=pos;
                        }
                    }
                    // if the second {
                    else if(c=='{'){
                        layer++;

                        // has prev :
                        if(prevColonPos!=-1){
                            // only collection top 2 layers
                            if(layer<=3){
                                //get type
                                tmpstr=code.substring(prevColonPos, pos).replace(/\s*/,'');
                                // function
                                if(tmpstr[0]=='f'){
                                    args=tmpstr.substring(tmpstr.indexOf('(')+1,tmpstr.indexOf(')')).split(',');
                                    if(args[0]==="")args=null;
                                    type='function';
                                }else if(tmpstr[0]=='{'){
                                    type='object';
                                } 
                                // get key
                                tmpstr2=code.substring(lastOKPos, prevColonPos-1);
                                tmpResult=keyPattern.exec(tmpstr2);
                                if(tmpResult!=null){
                                    key=tmpResult[0].replace(/[\'\"\s]/g,'');    
                                    if(layer==2 && (type=='object' || type=='function')){
                                        layer1Key=key;
                                        codefrom=pos-1;
                                    }
                                }
                            }
                            // use once only
                            prevColonPos=-1;
                            lastOKPos=pos;
                        }
                    }else if(c=='}'){
                        // has prev : [same1]
                        if(prevColonPos!=-1){
                            // only collection top 2 layers
                            if(layer<=3){
                                type='other';
                                subcode=ocode.substring(codefrom=prevColonPos,codeto=pos-spaceCount-1);
                            }
                            
                            // get key
                            tmpstr2=code.substring(lastOKPos, prevColonPos-1);
                            tmpResult=keyPattern.exec(tmpstr2);
                            if(tmpResult!=null){
                                key=tmpResult[0].replace(/[\'\"\s]/g,'');    
                            }

                            // use once only
                            prevColonPos=-1;
                            lastOKPos=pos;
                        }

                        layer--;
                        if(layer===0){
                            subcode=ocode.substring(codefrom=clsCodeStart-1,codeto=pos);
                            hash.code=subcode;
                            hash.from=codefrom;
                            hash.to=codeto;
                        }else if(layer===1){
                            if(layer1Key){
                                ood.set(hash.sub,[layer1Key,"to"],pos);
                                hash.arr.push(layer1Key);
                            }
                        }else if(layer===2){
                            subcode=ocode.substring(codefrom=codeStart-1,codeto=pos);
                            ood.set(hash.sub,[layer1Key,"sub",key,'code'],subcode);
                            ood.set(hash.sub,[layer1Key,"sub",key,'from'],codefrom);
                            ood.set(hash.sub,[layer1Key,"sub",key,'to'],codeto);
                        }
                        //
                        prevColonPos=-1;
                        lastOKPos=pos;
                    }
                    spaceCount=0;
                }
                
                if(type){
                    if(!hash.sub)hash.sub={};
                    if(layer1Key){
                        if(layer1Key!=key){
                            if(!ood.get(hash.sub,[layer1Key,"sub"]))
                                ood.set(hash.sub,[layer1Key,"sub"],{});
                            ood.set(hash.sub,[layer1Key,"sub",key],{
                                type:type,
                                args:args,
                                code:subcode,
                                from:codefrom,
                                to:codeto
                            });
                            hash.sub[layer1Key].arr.push(key);
                        }else{
                            ood.set(hash.sub,[layer1Key],{
                                type:"object",
                                from:codefrom,
                                arr:[]
                            });
                        }
                    }else{
                        hash.sub[key]={
                            type:type,
                            args:args,
                            code:subcode,
                            from:codefrom,
                            to:codeto
                        };
                    }
                    codeStart=pos;
                }
                
                if(layer===1&&c=='}'&&layer1Key)layer1Key=null;

                type=args=null;
            }while(pos<=len && layer>0)

            ood.breakO(cache);
            cache={};
            return cache[code+"^**^"+index]=hash;
        },
        getArgsFromAPI:function(cls,key,isInstance,firstArgsCode){
            var doc=ood.getRes("doc."+cls+(isInstance?".prototype":"")+"."+key);
            // try parent's member function
            if(ood.isStr(doc) && doc===key){
                var obj = ood.get(window,(cls+(isInstance?".prototype":"")+"."+key).split('.'));
                if(obj && obj.$original$)
                    doc= ood.getRes("doc." +obj.$original$ + '.prototype.'+key);
            }

            if(ood.isStr(doc) && doc===key)
                return null;

            if(doc){
                var rp,clso=ood.SC.get(cls);
                if(clso && clso.$EventHandlers)
                    rp=ood.fun.args(clso.$EventHandlers[key]);

                var paras=doc.$paras, arr=[], str1, str2;
                if(paras&&paras.length){
                    // the first argument is special
                    if(firstArgsCode){
                        arr=[{code:firstArgsCode}];    
                    }
                    for(var i=firstArgsCode?1:0;i<paras.length;i++){
                        if(clso && rp && clso.$DataModel && clso.$DataModel.items && rp[i]=="item"){
                            arr.push({code:"CONF.mixed_prop."+cls.replace(/\./g,"_")+"_item"});
                        }else if(clso && rp && cls=="ood.UI.TreeGrid" && rp[i]=="col"){
                            arr.push({code:"CONF.mixed_prop."+cls.replace(/\./g,"_")+"_col"});
                        }else if(clso && rp && cls=="ood.UI.TreeGrid" && rp[i]=="row"){
                            arr.push({code:"CONF.mixed_prop."+cls.replace(/\./g,"_")+"_row"});
                        }else if(clso && rp && cls=="ood.UI.TreeGrid" && rp[i]=="cell"){
                            arr.push({code:"CONF.mixed_prop."+cls.replace(/\./g,"_")+"_cell"});
                        }else{
                            str1=paras[i].replace(/(\w+).*/, '$1');
                            str2=paras[i].replace(/.*\:\s*(([\w]+\.)*[\w]+).*/, '$1');
                            arr.push({name:str1, type:((str2=="Event"||str2=="Element"||str2=="Document")?"Object":str2)+".prototype",ori:str2});
                        }
                    }
                    return arr;
                }
            }
            return null;
        },
        _alias:1,
        getClassStructWithScope:function(code,index,cacheKey1,cacheKey2){
            var ns=this,
                cache=ns._Cache1||(ns._Cache1={});
            if(cache[code+"^**^"+index])return cache[code+"^**^"+index];

            var hash=ns.getClassStruct2(code,index),
                _this=ns._this,t,t1;

            if(!hash)return null;
            
            // for those Class inner functions
            ood.arr.each(['Constructor', 'Dependency', 'Initialize', 'Before', 'After','$End'],function(i){
                if((t=ood.get(hash,["sub",i])) && t.type=='function'){
                    ood.set(hash,["sub",i],{
                        argsv:[_this,"''"],
                        _this:hash.pname?("__getThis('"+hash.pname+"',1)"):"{}"
                    });
                }
            });
            
            if(t=ood.get(hash,["sub","Instance","sub","initialize"])){
                t["_this"]=hash.pname?("__getThis('"+hash.pname+"',1)"):"{}";
            }

            // from parent Class
            if(hash.pname){
                if(t=ood.get(hash,["sub","Static","sub"])){
                    ood.each(t,function(o,key){
                        if(key==='sub')return;
                        //this
                        o["_this"]=hash.pname?("__getThis('"+hash.pname+"',1)"):"{}";
                        
                        //argsv
                        if(typeof(hash.pname)=='string'){
                            var arr=ns.getArgsFromAPI(hash.pname,key,false);
                            if(arr)o["argsv"]=arr;
                        }else{
                            ood.arr.each(hash.pname,function(cls){
                                var arr=ns.getArgsFromAPI(cls,key,false);
                                if(arr)o["argsv"]=arr;
                            })
                        }
                    });
                }
                if(t=ood.get(hash,["sub","Instance","sub"])){
                    ood.each(t,function(o,key){
                        //this
                        o["_this"]=hash.pname?("__getThis('"+hash.pname+"',0)"):"{}";
                        //argsv
                        if(typeof(hash.pname)=='string'){
                            var arr=ns.getArgsFromAPI(hash.pname,key,true);
                            if(arr)o["argsv"]=arr;
                        }else{
                            ood.arr.each(hash.pname,function(cls){
                                var arr=ns.getArgsFromAPI(cls,key,true);
                                if(arr)o["argsv"]=arr;
                            })
                        }
                    });
                }
            }
            // [[[[[ bak
            var bak=ood.Dom.pack,
                bak1=ood.absProfile.prototype.$link,
                bak11 = ood.absObj.$pickAlias;

            ood.Dom.pack=function(){var o=new ood.Dom(false),d=document.getElementById('abc');o._nodes=[d];return o};
            ood.absProfile.prototype.$link=function(){return this;};
            ood.absObj.$pickAlias = function(){return ns._alias++};
            // ]]]]]
            try{
                var globalCache;
                if(cacheKey1 && cacheKey2){
                    globalCache=window[cacheKey1] || (window[cacheKey1]={});
                    // for cache Class's runtime struct
                    if(globalCache[cacheKey2]){
                         // destroy last time created controls
                         if(ood.get(globalCache[cacheKey2],["Instance","_ctrlpool"])){
                            ood.each(globalCache[cacheKey2].Instance._ctrlpool,function(o,i){
                                if(o&&!o.destroyed){
                                    if(o.destroy)o.destroy();
                                    if(o.boxing && o.boxing())o.boxing().destroy();
                                }
                            });
                            globalCache[cacheKey2].Instance._ctrlpool=null;
                        }
                        ood.breakO(globalCache[cacheKey2]);
                        globalCache[cacheKey2]={};
                    }else {
                        globalCache[cacheKey2]={};
                    }
                    globalCache=globalCache[cacheKey2];
                }else{
                    globalCache={}
                }

                globalCache['Instance']={_ctrlpool:{},properties:{},events:{}};
                globalCache['Static']={};
                if(t1=ood.get(hash,['sub','Instance','sub'])){
                    ood.each(t1,function(o,i){
                        try{
                            var ecode = o.type=='function' ? ("(function("+(o.args?o.args.join(","):"")+"){})") : ("("+o.code+")");
                            globalCache['Instance'][i] = eval(ecode);
                            ecode=null;
                        }catch(e){globalCache['Instance'][i]=null}
                    });
                }
                if(t1=ood.get(hash,['sub','Static','sub'])){
                    ood.each(t1,function(o,i){
                        try{
                            var ecode = o.type=='function' ? ("(function("+(o.args?o.args.join(","):"")+"){})") : ("("+o.code+")"); 
                            globalCache['Static'][i] = eval(ecode);
                            ecode=null;
                        }catch(e){globalCache['Static'][i]=null}
                    });
                }
                
                // from dynamic (ood.Module only)
                // modified: try it for all class
                //if(hash.isModule){
                    // Instance.events
                    if(t=ood.get(hash,['sub','Instance','sub','events'])){
                        if(t=t.code){
                            try{
                               var ecode="("+t+")";
                               t=eval(ecode);
                               ecode=null;
                            }catch(e){t=null}
                            if(t&&ood.isHash(t)){
                                ood.each(t,function(evt,name){
                                    if(ood.isArr(evt))
                                        for(var i=0,l=evt.length;i<l;i++)
                                            if(typeof(evt[i])=="string"){
                                                evt=evt[i];
                                                break;
                                            }
                                    if(typeof(evt)=="string"){
                                        if(t1=ood.get(hash,['sub','Instance','sub',evt])){
                                            //argsv
                                            t1["argsv"]=ns.getArgsFromAPI("ood.Module",name,true,"this");
                                        }
                                    }
                                });
                            }
                        }
                    }
                    // Instance properties
                    if(t=ood.get(hash,['sub','Instance','sub','initialize'])){
                        if(t=t.code){
                            try{
                                var ecode="(function()"+t+")"
                                t=eval(ecode);
                                ecode=null;
                            }catch(e){t=null}
                            if(typeof(t)=='function'){
                                try{
                                    globalCache.Instance.$inDesign="xxx";
                                    t.call(globalCache.Instance);
                                }catch(e){}
                            }
                        }
                    }
                    if(t=ood.get(hash,['sub','Instance','sub','iniComponents'])){
                        if(t=t.code){
                            try{
                                // create lots of controls, have to destroy it somewhere
                                var ecode="(function()"+t+")";
                                t=eval(ecode);
                                ecode=null;
                            }catch(e){t=null}
                            if(typeof(t)=='function'){
                                try{
                                    globalCache.Instance.$inDesign="xxx";
                                    t.call(globalCache.Instance);
                                }catch(e){}
                                
                                // for all events
                                ood.each(globalCache.Instance._ctrlpool,function(o,i){
                                    var cls = o.key;
                                    ood.each(o.getEvents(),function(evt,key){
                                        if(ood.isArr(evt))
                                            for(var i=0,l=evt.length;i<l;i++)
                                                if(typeof(evt[i])=="string"){
                                                    evt=evt[i];
                                                    break;
                                                }
                                        if(typeof(evt)=="string"){
                                            if(t1=ood.get(hash,['sub','Instance','sub',evt])){
                                                //argsv
                                                t1["argsv"]=ns.getArgsFromAPI(cls,key,true, "(new "+cls+").get(0)");
                                            }
                                        }
                                    });
                                });
                                // must destroy here
                                ood.asyRun(function(){
                                    ood.each(globalCache.Instance._ctrlpool,function(o,i){
                                        if(o&&!o.destroyed){
                                            if(o.destroy)o.destroy();
                                            if(o.boxing && o.boxing())o.boxing().destroy();
                                        }
                                    });
                                    ood.breakO(globalCache.Instance)
                                });
                            }
                        }
                    }
                //}
            }finally{
            //[[[[[ restore
                ood.Dom.pack=bak;
                ood.absProfile.prototype.$link=bak1;
                ood.absObj.$pickAlias = bak11;
            //]]]]]
            }
    	    ood.breakO(cache);
    	    cache={};
            return cache[code+"^**^"+index]=hash;
        },
        // To get min code
        getMinCode : function(code,index){
            if(index<0 || index>=code.length)return "";
var t1=new Date;
            var reg2=/(\{(((\{\})|[^\{\}\x01])+)\})|(\[([^\[\]0\x01]+)\])/,
                reg3=/(((\s*function\s*([\w$][\w]*\s*)?\(\s*([\w$\s,]*)\s*\)\s*)?(\{(((\{\})|[^\{\}\x01])*)\}))|(\[([^\[\]0\x01]+)\]))/g;
        
            // add special char    
            code = code.replace(/\x01/g,'0');
            code = code.substring(0,index) + "\x01"+ code.substring(index+1,code.length);

            // clear string/reg,comments
            code = ood.replace(code, [
                // simplify some
                [/\r\n?|\n/,"\n"],
                [/(\uFEFF|\xA0|[\t\x0B\f])/, " "],
                //protect {"a":1}
                [/"[\w\$][\w\.]*"\s*\:/,'$0'],
                [/'[\w\$][\w\.]*'\s*\:/,"$0"],
                // clear string
                [/"(\\.|[^"\\\n])*"/,'"a"'],
                [/'(\\.|[^'\\\n])*'/,"'a'"],
                // clear reg
                [/\/(\\[\/\\]|[^*\/])(\\.|[^\/\n\\])*\/[gim]*/,'(new RegExp())'],
                // clear comments
                [/\/\*[^*]*\*+([^\/][^*]*\*+)*\//,''],
                [/\/\/[^\n]*/,'']
                ]);
var t2=new Date;
//console.log(t2-t1);            
            // clear all function(){}, {} and []
            while(reg2.test(code)){
                code = code.replace(reg3, function(a){
                    return a[0]=="{"?"{}":a[0]=="["?"[0]":"(new Function())"
                }); 
            }
var t3=new Date;
//console.log(code,t3-t2);            
            var i=0,k=code.indexOf('\x01'),l=code.length,ignoreComma=true,
                a1=[],a2=[],a3=[],arr2=[],arr3=[],c,tmp=0,strtmp,prevDo=true;
            
            // if code not in any function, return 
            var tests=code.substring(0,k);
            if(!/\bfunction\b/.test(tests)){
                return tests;
            }
        
            // clear those useless code
            do{
                c=code[i++];
                if(i<k){
                    // if the second {
                    if(c=='{' && code[i]!='}'){
                        strtmp=code.substring(tmp,i+1);
                        arr2.push(strtmp+"\n");
                        // if found the outter function, stop search next {
                        if(/\:\s*function\b/.test(strtmp)){
                            arr2.push(code.substring(i+1,k)+"\n");
                            i=k;
                            continue;
                        }
                        ignoreComma=false;
                    }
                    // keep the pos of { or ,
                    if((c=='{' && code[i]!='}')||((!ignoreComma)&&c==','))
                        tmp=i;
                    if(c=="("){
                        if(code.substring(i-1,i+13)=="(new RegExp())"){
                            i+=13;
                        }else if(code.substring(i-1,i+15)=="(new Function())"){
                            i+=15;
                        }else{
                            ignoreComma=true;
                        }
                    }
                }else{
                    if(prevDo){
                        arr2.push("{}");
                        prevDo=false;
                    }
                    if(c=="{"){
                        a1.push(c);
                    }else if(c=="}"){       
                        if(a1.length===0)arr3.push(c);else a1.pop();
                    }else if(c=="["){
                        a2.push(c);
                    }else if(c=="]"){       
                        if(a2.length===0)arr3.push(c);else a2.pop();
                    }else if(c=="("){
                        a3.push(c);
                    }else if(c==")"){       
                        if(a3.length===0)arr3.push(c);else a3.pop();
                    }
                }
            }while(i<l)

            return arr2.join('\n').replace(/\s*\n\s*/g,'\n')+arr3.join('');
        },
        
        // To get content vars(global and local)
        getContextVars : function(code,index){
            code=code.substring(0,index);
            // use CodeMirror function
            var cm = new CodeMirror(new Function);
            cm.setValue(code);
            
            var hash={},funPos,
                // get vars from CodeMirror's state
                getVars=function(obj,hash){
                    vars=obj && obj.vars;
                    if(vars)do{hash[vars.name]=1}while(vars=vars.next);
                    if(obj && obj.prev)
                        getVars(obj.prev,hash);
                }; 
            var state=cm.getStateAfter(cm.doc.posFromIndex(index).line, true),vars;

            // get from local
            vars=state.localVars;
            if(vars)do{hash[vars.name]=1}while(vars=vars.next);

            // get from global
            vars=state.globalVars;
            if(vars)do{hash[vars.name]=1}while(vars=vars.next);

            // try to get from content
            getVars(state.context,hash);

            return hash;
        },
        getContextVars_fast : function(cm,index){            
            var hash={},funPos,
                // get vars from CodeMirror's state
                getVars=function(obj,hash){
                    vars=obj && obj.vars;
                    if(vars)do{hash[vars.name]=1}while(vars=vars.next);
                    if(obj && obj.prev)
                        getVars(obj.prev,hash);
                }; 
            var state=cm.getStateAfter(cm.doc.posFromIndex(index).line, false),vars;

            // get from local
            vars=state.localVars;
            if(vars)do{hash[vars.name]=1}while(vars=vars.next);

            // get from global
            vars=state.globalVars;
            if(vars)do{hash[vars.name]=1}while(vars=vars.next);

            // try to get from content
            getVars(state.context,hash);

            return hash;
        },
        // To get content vars assign code(from top to bottom)
        getContextAssignments : function(code, index){
            code=this.getMinCode(code, index);
//console.log(code);
            index=code.lastIndexOf('\n');
            // remove all extra spaces
            code=code.replace(/[ ]+/g," ");

            // remove all empty Class
            code=code.replace(/\b(ood.)?Class\s*\(\s*["'\w\$\.]+\s*\,\s*["'\w\$\.]+\s*\,\s*\{\}\s*\)\s*(;)?/g,"");

            var len=code.length,
                funpatt=/\s*function\s*([\w$]+\s*)?\(\s*[\w$\s,]*\s*\)\s*\{/g;
        
            // Is it an ood Class?
            var regoodCls = /\b(ood.)?Class\s*\(\s*["'\w\$\.]+\s*\,\s*["'\w\$\.]+\s*\,\s*\{/,
                isOODClass = regoodCls.exec(code);
            // If it's an ood Class, add special scope code
            if(isOODClass!=null){
                // from the first {
                var i=regoodCls.lastIndex+isOODClass[0].length,
                    // special scope key/type/start pos/end pos
                    firstType=null,firstKey=null,secondType=null,secondKey=null,
                    specialScopeS=-1,specialScopeE=len,specialVarDef=[],
                    keepi=i,colon,c;
        
                // loop to get special scope info
                while(i<len){
                    c=code[i++];
                    if(c==" " || c=="\n")continue;
                    if(colon){
                        // first layer is hash
                        if(c=="{"){
                            firstType='hash';
                            firstKey=code.substring(keepi+1,i-1).replace(/[\'\" \n:]/g,'');
                            
                            // to get second layer info
                            keepi=i;
                            colon=false;
                            while(i<len){
                                c=code[i++];
                                if(c==" " || c=="\n")continue;
                                if(colon){
                                    // the second layer of ood Class
                                    // special scope from
                                    if(funpatt.test(code.substring(i-1,len))){
                                        secondType='function';
                                        secondKey=code.substring(keepi+1,i-1).replace(/[\'\" \n:]/g,'');
                                        specialScopeS=i-1;                                
                                        if(funpatt.test(code.substring(i-1,len))){
                                            // special scope to
                                            specialScopeE=funpatt.lastIndex+i-1;
                                        }
                                        
                                        // to get function's arguments
                                        var stra="",startC;
                                        while(i<len){
                                            c=code[i++];
                                            if(c==" " || c=="\n")continue;
                                            if(c=="("){startC=1;continue;}else if(c==")"){break;}
                                            if(startC){stra+=c;}
                                        }
                                        specialVarDef=stra.split(',');
        
                                    }else{
                                        //ignore others
                                    }
                                    break;
                                }
                                // the fist :
                                colon = c==":";
                            }
                        // firts layer is function
                        }else if(c=="f"){
                            // special scope from
                            firstType='function';
                            firstKey=code.substring(keepi+1,i-1).replace(/[\'\" \n:]/g,'');
                            specialScopeS=i-1;
                            if(funpatt.test(code.substring(i-1,len))){
                                // special scope to
                                specialScopeE=funpatt.lastIndex+i-1;
                            }
                            
                            // to get function's arguments
                            var stra="",startC;
                            while(i<len){
                                c=code[i++];
                                if(c==" " || c=="\n")continue;
                                if(c=="("){startC=1;continue;}else if(c==")"){break;}
                                if(startC){stra+=c;}
                            }
                            specialVarDef=stra.split(',');
        
                        }else{
                            //Can not be other
                        }
                        break;
                    }
                    // find the fist :
                    colon = c==":";
                }
            }
            
            //collect var assign code
            var arr1=[],arr2=[],scopeS=-1,scopeE=-1,result,added=false,
                // var assignment pattern
                patt=/([\w$][\w]*)\s*\=\s*([^\s]{1})/g,
                codeL=code.length,
                inSpecialScope=false;
        
        outerloop:
            while ((result = patt.exec(code)) != null)  {
                //add code for special function arguments
                if(specialScopeS!=-1){
                    if(patt.lastIndex>=specialScopeS){
                        if(patt.lastIndex<specialScopeE){
                            inSpecialScope=true;
                            if(!added){
                                scopeS=arr1.length;
                                added=true;
                            }
                        }else{
                            inSpecialScope=false;
                            scopeE=arr1.length;
                        }
                    }else{
                        inSpecialScope=false;
                    }
                }
        
                var v2=result[2];
                
                // get var assign code
                if('='==v2[0]){
                    //ignore ==
                    continue outerloop;
                }else if('{'==v2[0]){
                    v2="{}";
                }else if('['==v2[0]){
                    v2="[]";
                }else{
                    var j=patt.lastIndex-1,cc,count=0,prevIsOperator=false,enter=false;;
                    while(j<=codeL){
                        cc=code[j++];
                        if(cc==" ")continue;
                        if(prevIsOperator){
                            prevIsOperator=false;
                        }
                        if(enter){
                            enter=false;
                            if(!(cc=="+"||cc=="-"||cc=="*"||cc=="/"||cc==".")){
                                break;
                            }
                        }
        
                        // match ()
                        if(cc=="("){
                            count++;
//                        }else if(cc==")"||cc=="}"){
                        }else if(cc==")"){
                            if(count>0)count--;
                            // stop at )
                            else{
                                 break;
                            }
                        }else if(cc=="+"||cc=="-"||cc=="*"||cc=="/"||cc=="."){
                            prevIsOperator=true;
                            continue;
                        }
                        // if in (), continue
                        if(count>0){
                            continue;
                        }else{
                            // stop at ,;
                            if(cc==","||cc==";"){
                                 break;
                            }else if(cc=="\n"){
                                if(prevIsOperator)continue;
                                else{
                                    break;
                                }
                            }
                        }
                    }
                    v2=code.substring(patt.lastIndex-1,j-1);
                    if(/\bfunction\s*\(/.test(v2)){
                        v2="(new Function())";
                    }
                    // avoid: try{(b=ns.);}catch(e){};
                    if(/\.$/.test(v2)){
                        v2=v2.replace(/[\.]+$/,'');
                    }
                }
                arr1.push(result[1]);
                arr2.push(v2);
            };
            return {vars:arr1,code:arr2,
                firstType:firstType,
                firstKey:firstKey,
                secondType:secondType,
                secondKey:secondKey,
                scopeS:scopeS,
                scopeE:scopeS==-1 ? -1 : (scopeE==-1?arr1.length:scopeE),
                outscope:scopeS==-1 ? false : index>=specialScopeE };
        }
    }
});
