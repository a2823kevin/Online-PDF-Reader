"use client"
import { useContext } from "react";
import { AppCtx } from "@/ctx";
import { HamburgerMenuIcon } from "@radix-ui/react-icons";

const MenuToggle = () => {
    const { menuOpen, toggleMenu } = useContext(AppCtx);
    return (
        <div className='p:5% bg:red-40 border-radius:4px border:red|1px|solid user-select:none cursor:pointer'
        onClick={()=>{toggleMenu();}}>
            <HamburgerMenuIcon className={`w:3.5vh h:3.5vh ${menuOpen?"rotate(90deg)":""} transition:0.5s`} />
        </div>
    )
}

export default MenuToggle;