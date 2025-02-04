"use client"
import { useContext, useEffect } from "react";
import AuthDialog from "@/components/authDialog";
import { AppCtx } from "@/ctx";

const Account = () => {
    const { accountId, setAccountId } = useContext(AppCtx);
    useEffect(() => {
        async function getAuthInfo() {
            const id = (await (await fetch("/OnlinePDFReader/auth", {
                method: "GET"
            })).json()).id;
            setAccountId(id);
        }
        getAuthInfo();
    }, []);

    return (
    <>
        {accountId==="-1"?
            <div className="flex flex>div center-content>div px:1vw>div cursor:pointer>div bg:red-55>div:hover transition:0.3s>div">
                <AuthDialog type="login" triggeringComponent={<div>Log in</div>} />
                <AuthDialog type="signup" triggeringComponent={<div>Sign up</div>} />
            </div>
            :
            <div className="flex flex>div center-content>div px:1vw>div bg:red-55>div:hover transition:0.3s>div">
                <div>{accountId}</div>
                <div className="cursor:pointer" onClick={()=>{fetch("/OnlinePDFReader/auth/logout", {method: "POST"}); setAccountId("-1");}}>Log out</div>
            </div>
        }
    </>
    )
}

export default Account;