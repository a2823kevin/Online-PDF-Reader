"use client"
import * as Dialog from "@radix-ui/react-dialog";
import AuthButton from "@/components/authButton";
import { useState } from "react";

const AuthDialog = (props) => {
    const [diaLogOpen, setDiaLogOpen] = useState(false);
    const [id, setId] = useState("");
    const [password, setPassword] = useState("");

    return (
        <Dialog.Root open={diaLogOpen} onOpenChange={setDiaLogOpen}>
            <Dialog.Trigger asChild>
                {props.triggeringComponent}
            </Dialog.Trigger>
            <Dialog.Portal>
                <Dialog.Overlay className='abs top:0 left:0 w:100vw h:100vh bg:black opacity:0.75 transition:0.3s' />
                <Dialog.Content>
                    <div className='abs top:25vh left:25vw w:50vw h:50vh p:5vh bg:white border-radius:2.5rem transition:0.3s'>
                        <div className='flex center-content mb:5vh'><h1 className="user-select:none">{props.type === "login" ? "login to access your pdfs" : "sign up to use pdf reader"}</h1></div>
                        <div className='flex center-content gap:2.5vw'>
                            <div className='flex flex-direction:column align-items:end gap:1vw flex>div gap:1vw>div'>
                                <div>
                                    <h3>id</h3>
                                    <input onChange={(evt) => { setId(evt.target.value) }} required="required"></input>
                                </div>
                                <div>
                                    <h3>password</h3>
                                    <input type="password" onChange={(evt) => { setPassword(evt.target.value) }} required="required"></input>
                                </div>
                            </div>
                            <AuthButton id={id} password={password} type={props.type} setDiaLogOpen={setDiaLogOpen} />
                        </div>
                    </div>
                </Dialog.Content>
            </Dialog.Portal>
        </Dialog.Root>
    )
}

export default AuthDialog;