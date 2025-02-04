"use client"
import { AppCtx } from "@/ctx";
import { useContext, useState } from "react";

const AuthButton = (props) => {
    const { setAccountId } = useContext(AppCtx);
    const [loginStatus, setLoginStatus] = useState(0);
    return (
        <div className={`flex center-content w:12.5vh h:12.5vh cursor:${props.id.length>0&&props.password.length>0?"pointer":"not-allowed"} ${props.id.length>0&&props.password.length>0?"opacity:0.75:hover":"opacity:0.4"} border-radius:1.5rem color:white bg:${props.type==="login"?"gray":"orange"} transition:0.3s`}
             onClick={props.id.length>0&&props.password.length>0?async ()=>{
                setLoginStatus(-1);
                const resp = await (await fetch(`/OnlinePDFReader/auth/${props.type}`, {
                    method: "POST", 
                    body: JSON.stringify({
                        id: props.id, 
                        password: props.password
                    })
                })).json();
                
                if (resp.status=="Success") {
                    setAccountId(props.id);
                    props.setDiaLogOpen(false);
                }
                else {
                    alert(props.type==="login"?"Login failed!":"Sign up failed!");
                }
                setLoginStatus(0);
             }:undefined}>
            {loginStatus===0?(props.type==="login"?"Login":"Sign up"):(props.type==="login"?"Login...":"Sign up...")}
        </div>
    )
}

export default AuthButton;