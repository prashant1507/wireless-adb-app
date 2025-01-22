## Root Samsung Devices
1. Unlock the Boot Loader
    - Enable 'Developer options'
    - Enable 'OEM Unlocking'
        - If 'OEM Unlocking' is not available then follow:
            - Goto Settings -> Developer options
            - Disable 'Auto update system'
            - Set Time to Manual
            - Select time like 3 weeks in future
            - Enable 'Auto update system'
            - Close Settings app
            - Connect to internet
            - Verify 'OEM Unlocking' option
            - If present then enable it and follow the instructions

2. Download the Firmware
    - Goto https://www.samfw.com
    - Search device using model name (Settings -> About phone -> Model Name)
    - Select the country (consider chipset type in this case)
    - Note 'Bit/SW Rev.' from the Build Number from the device. 'Bit/SW Rev.' is the 5th character from the last
        - For e.g., If 'Build Number' is TP1A.220624.014.S918BXXS1AWD1 then 'Bit/SW Rev.' is 1
    - Download the lasted firmware of the same 'Bit/SW Rev.'
        - Once 'Bit/SW Rev.' is upgraded the download is not possible
        - Upgrade based on the requirement
    - Extract the zip files

3. Download Magisk app from https://github.com/topjohnwu/Magisk and install in the device

4. Prepare Magisk AP file
    - Copy the AP_* file from the extracted file from Step 2 to the device
    - Open Magisk app -> click install which is next to Magisk
    - Click Select and Patch a File and select the AP file.
    - After patching, you should transfer the patched AP file (its usually renamed to Magisk Patched or something.) to the pc
5. Download Odin3 Flashing software from https://odindownloader.com/download/ for Windows
    - Connect the device to the machine using USB
    - Enter the device in download mode (adb restart bootloader)
    - Open Odin3 -> Go to Options and Disable Auto Reboot.
    - Click the AP button and choose the patched magisk file.
    - As we added the AP file, add the default. (USERDATA is HOME_CSC_*)
    - Click Start

6. Once done follow the steps in the device