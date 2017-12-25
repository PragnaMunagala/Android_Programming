finalmatrix=zeros(10000,64*110);

% Get a list of all files and folders in this folder.
files = dir('Dataset-20171120T203537Z-001/Dataset/')
% Get a logical vector that tells which is a directory.
dirFlags = [files.isdir]
% Extract only those that are directories.
subFolders = files(dirFlags)
% Print folder names to command window.

% for k = 3 : length(subFolders)
for k = 3 : 12
    result=zeros(1000,64);
    F = dir(strcat('Dataset-20171120T203537Z-001/Dataset/',subFolders(k).name,'/*.edf'));
    [~,record] = edfread(strcat('Dataset-20171120T203537Z-001/Dataset/',subFolders(k).name,'/',(F(1).name)));
    record(end,:) = [];
    
    result = record';

    for ii = 2:length(F)
       [~,record] = edfread(strcat('Dataset-20171120T203537Z-001/Dataset/',subFolders(k).name,'/',(F(ii).name)));
       record(end,:) = [];
       record=record';
       result = vertcat(result,record);
    end

    userlabel=k-2;
    append_column(1:size(result,1),1)=userlabel;
    result=[result append_column];
    if k==3
        finalmatrix=result;
    else
        finalmatrix=vertcat(finalmatrix,result);
    end
    append_column=[];
end


csvwrite('finaldataset.csv',finalmatrix);


