//RAD Main Application
(function () {
    ood.set(ood.Locale, ["en", "RAD"], {

    soon: 'Coming soon',
    jumpto: 'Go to line',
    ok: 'OK',
    cancel: 'Cancel',
    'close': 'Close',
    save: 'Save',
    Load: 'Load',
    Clear: 'Clear',
    notsave: 'Not saved',
    notsave2: 'The window you are about to close has not been saved. Do you want to discard changes and continue closing?',
    notsave3: 'There are unsaved documents in the current project. Do you want to continue?',
    checkOK: 'Code has no syntax errors!',
    en: 'English',
    cn: 'Chinese',
    ja: 'Japanese',
    tw: 'Traditional Chinese',
    ru: 'Russian',
    langTips: 'Change language',
    selFile: "Select document",
    selFilePath: "Please select a document path first",
    'Remove': 'Delete confirmation',
    Message: "Message",
    'Error message': 'Error message',
    'Remainder: Update ESDUI RAD to the Latest Version': 'New version reminder',
    'Are You Sure to remove this row': 'Are you sure to delete this row?',
    'Are You Sure to remove all': 'Are you sure to delete all?',
    "Move/Switch Workspace": "Move or switch workspace",
    "ESDUI stores your projects in a  folder, called OODWorkspace. Choose a folder to move/switch your OODWorkspace to": "Workspace is a local folder where ESDUI stores your projects. You can move or switch your workspace to the following selected folder",
    "Select target folder": "Please select",
    'Move': "Move workspace to",
    'Switch': "Switch workspace to",
    "Target Folder": "Target folder",
    "Switch OOD Workspace to following target folder, move all project files into it, and remove the old OOD Workspace folder": "Switch workspace to target folder and move all projects from current workspace",
    "Switch OOD Workspace to following target folder only, don't move any project files, don't remove the old OOD Workspace": "Only switch workspace to target folder, do not move projects from current workspace",
    "Your OODWorkSpace has been $0 to '$1' successfully": "Workspace has been successfully $0 to '$1'",

    Search: "Search",
    download: {
        "Before the first deploy for this platform, we need to download the corresponding packager": "This is the first deployment for this platform, need to download the corresponding packager first",
        "Cancel Download": "Cancel download",
        "will be download to": "Will be downloaded to the following directory",
        "Extracting": "Extracting, please wait",
        "Download was aborted": "Download was aborted",
        "In order to deploy the application, you have to try it again later": "Need to download packager before deploying application, please try again later",
        'Project folder exists already, specify another project name please!': 'Project folder already exists, please use another project name!',
        '\"$0\" or the corresponding resource file exists already, specify another name please!': '\"$0\" or corresponding resource file already exists, please use another name!',
        'URL $0 doest exist': 'URL path $0 is invalid!',
        'Package $0 doest exist': 'OOD template package $0 does not exist!',
        'File $0 is not a ood template': '$0 is not a valid OOD template package!',
        '$0 dones not exit': '$0 does not exist!',
        "$0 '$1' already exists": "$0 '$1' already exists",
        "Click there to download ESDUI Desktop Version": "Click here to download ESDUI Desktop Version"
    },
    msg: {
    },
    about: {
        description: "Cloud-native code tool jointly launched",
        action1: "Action 1",
        contact: "Contact: 18618287247",
        wechat: "WeChat: wenzhanglwz"
    },
    img: {
        'Search in Category': 'Search in current category',
        'Image Selector': 'Please select image path',
        "Local Gallery": 'Local gallery',
        "Online Gallery": 'Remote gallery',
        'Images in the Project': 'Images in this project',
        'Local Disk': 'Local disk images',
        'Upload a Picture': 'Upload picture',
        'Internet Picture': 'Internet picture',
        'Categories': 'Categories',
        "URL": 'Image URL',
        "Type picture URL here": 'Please enter image URL here',
        "This is not a valid URL address": "This is not a valid URL address",
        'Fechting image': 'Fetching image',
        'Allows $0 only': "Only allow $0 images"
    },
    RADAbout: {
        description: "Cloud-native code tool jointly launched",
        action1: "Action 1",
        contact: "Contact: 18618287247",
        wechat: "WeChat: wenzhanglwz"
    },
    RAD: {
        actions: {
            dynamicLoad: "Dynamic load component",
            validateData: "Validate data"
        },
        addfile: {
            importFile: "Import file"
        },
        addproject: {
            newProject: "New project",
            projectName: "Project name:",
            chineseName: "Chinese name",
            publishAddress: "Publish address:",
            templateName: "Template name:",
            projectType: "Project type:",
            creating: "Creating..."
        },
        apiconfig: {
            remoteURL: "Remote URL:",
            autoRun: "Auto run",
            cascade: "Cascade",
            paramUsage: "Parameter usage",
            componentName: "Component name",
            mappingAddress: "Mapping address",
            componentType: "Component type",
            noAPIFound: "No matching API found!"
        },
        api: {
            webAPIManagement: "WEB-API Management",
            pagination: "Pagination",
            name: "Name",
            path: "Path",
            serviceClass: "Service class",
            description: "Description",
            serverAddress: "Server address",
            add: "Add",
            remove: "Remove",
            refresh: "Refresh",
            apiList: "API list",
            operation: "Operation",
            edit: "Edit",
            delete: "Delete",
            view: "View"
        },
        apitree: {
            allAPIs: "All APIs"
        },
        widgets: {
            layoutContainer: "Layout container",
            layout: "Layout",
            leftNavButtons: "Left navigation buttons",
            navBar: "Navigation bar",
            topTabs: "Top tabs",
            tabSwitcher: "Tab switcher",
            foldingSwitcher: "Folding switcher",
            folding: "Folding",
            foldingTabs: "Folding tabs",
            collapsible: "Collapsible",
            menuNav: "Menu navigation",
            treeNav: "Tree navigation",
            commonContainers: "Common containers",
            popupPanel: "Popup panel",
            regularPanel: "Regular panel",
            groupContainer: "Group container",
            innerBlock: "Inner block",
            floatingLayer: "Floating layer",
            autoLayoutBlock: "Auto layout block",
            autoAlignedBlock: "Auto aligned block",
            autoAlignedPanel: "Auto aligned panel",
            relativePanel: "Relative panel",
            form: "Form",
            commonComponents: "Common components",
            regularButton: "Regular button",
            fileUpload: "File upload",
            hiddenField: "Hidden field",
            inputField: "Input field",
            contentBox: "Content box",
            selection: "Selection",
            password: "Password:",
            textLabel: "Text label",
            compositeComponents: "Composite components",
            compositeBox: "Composite box",
            richText: "Rich text",
            datePicker: "Date picker",
            timePicker: "Time picker",
            dateTime: "Date",
            progressBar: "Progress bar",
            slider: "Slider",
            optionList: "Option list",
            aggregateApp: "Aggregate application",
            aggregateModule: "Aggregate module",
            title: "Title",
            name: "Name",
            description: "Description",
            commonOpinions: "Common opinions",
            executeIfAgree: "Execute if agree!",
            messageAggregation: "Message aggregation",
            infoSubmission: "Information submission",
            more: ">> More",
            dailyApproval: "Daily approval",
            meetingNotice: "Meeting notice",
            leaveRequest: "Leave request",
            infoSummary: "Information summary",
            reportInfo: "Report information",
            administrator: "Administrator",
            item1: "item1",
            item2: "item2",
            messageCommunication: "Message communication",
            styleAnimation: "Style animation",
            menu: "Menu",
            paginationBar: "Pagination bar",
            pagination: "Pagination",
            mobileComponents: "Mobile components",
            tileBlock: "Tile block",
            bottomNavBar: "Bottom navigation bar",
            home: "Home",
            todo: "Todo",
            draft: "Draft"
        }
    },
    
    // DataModel translations
    DataModel: {
        iconColors: "Icon colors list",
        vertical: "Vertical adjustment"
    },
    
    // UI translations
    UI: {
        slider: {
            rangeStart: "Range start",
            rangeEnd: "Range end",
            value: "Current value"
        },
        progressbar: {
            value: "Progress value"
        },
        audio: {
            play: "Play",
            pause: "Pause",
            volume: "Volume"
        },
        Resizer: {
            resize: "Resize"
        }
    }
})();