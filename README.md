## First test task of the _Doczilla_ platform
### 🟦 Description:
The application reads specified (more on it in the **"ApplicationStart"** section below) text files' content and sorts it based on dependencies of one file to another, if applicable (if there are _require_ directives). The application handles cases of **cyclic dependency**, where one file depends on the other and that file depends on the first, for example, **when _File1_ depends on _File2_ and _File2_ depends on _File1_**.

The application works entirely in the console (_stdout_).

#### ℹ The _require_ directive:

Each file can have one, many or none _require_ directives, which dictates the dependency of one file on another. It is used for sorting, which works as follows:\
_if File1 is dependent on File2, then File1 is located below File2 in the sorted list_.

If none of the files contain the _require_ directive, the sorting is ignored and the output is based on the order of the file paths in the ```FILE_PATHS``` list of the ```Configuration``` class.


### 🟩 Application Start:
The application can be started by running the ```main``` method of the ```Application``` class.\
The results will show up in the console (_stdout_).

### 🟪 Configuration:
The application can be configured by specifying the paths of files, which need to be processed and sorted, in the ```FILE_PATHS``` list, located in the ```config``` package, in the ```Configuration``` class.
#### Important notes:
The file paths of the text files must be specified starting from the project folder, i.e. if, for example, the files are located in the ```src``` folder, then the file paths will look like ```src/*filename*.txt```

### ⛔ Constraints:
* The application cannot detect the text files automatically - the file paths need to be specified in the ```Configuration``` class (see the **Configuration** section for instructions).
* The application can only work with .txt files.